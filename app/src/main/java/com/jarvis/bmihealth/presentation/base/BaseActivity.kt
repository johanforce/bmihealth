package com.jarvis.bmihealth.presentation.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.lifecycle.ViewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.jarvis.bmihealth.MainApplication
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.presentation.pref.AppPreference
import com.jarvis.bmihealth.presentation.pref.AppPreferenceKey
import com.jarvis.bmihealth.presentation.pref.ThemeMode
import com.jarvis.bmihealth.presentation.pref.ThemeHelper
import com.jarvis.design_system.toolbar.JxToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<B : ViewBinding, T : ViewModel>(val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity(), CoroutineScope {
    protected val binding: B by lazy { bindingFactory(layoutInflater) }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job

    open fun initDarkMode() {
        val appPreference = AppPreference.getInstance()

        setTheme(R.style.DSAppTheme)
        ThemeHelper.applyTheme(2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.initDarkMode()
        super.onCreate(savedInstanceState)
        job = Job()
        initAnim()
        setContentView(binding.root)
        initToolbar()
        observeData()
        setUpViews()
        initCoroutineScope()
    }

    open fun setUpViews() {}

    open fun initCoroutineScope() {
        launch {
            setupDatas()
        }
    }

    open suspend fun setupDatas() {

    }

    open fun observeData() {}

    private fun initAnim() {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        val exit = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {

            // Only run the transition on the contents of this activity, excluding
            // system bars or app bars if provided by the app’s theme.
            addTarget(binding.root)
        }
        window.exitTransition = exit.addTarget(binding.root)
        window.allowEnterTransitionOverlap = true
    }

    override fun onLowMemory() {
        super.onLowMemory()
        finish()
    }

    protected open fun getToolbar(): JxToolbar? {
        return null
    }

    fun initToolbar() {
        val toolbar = getToolbar() ?: return
        setSupportActionBar(toolbar.toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val toolbar = getToolbar()
        return toolbar?.onCreateOptionsMenu(this, menu) ?: super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            onBackPressed()
        }
        val toolbar = getToolbar()
        return toolbar?.onOptionsItemSelected(item) ?: super.onOptionsItemSelected(item)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
//        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out)
    }

    protected fun startActivity(clazz: Class<*>?) {
        val intent = Intent(this, clazz).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    protected fun startActivity(clazz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(this, clazz).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtras(bundle!!)
        startActivity(intent)
    }

    override fun onDestroy() {
        job.cancel() // cancel the Job
        super.onDestroy()
    }
}
