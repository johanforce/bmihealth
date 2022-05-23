package com.jarvis.bmihealth.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<Binding : ViewBinding, ViewModel : BaseViewModel>(val bindingFactory: (LayoutInflater) -> Binding) :
    Fragment(), CoroutineScope {
    protected val binding: Binding by lazy { bindingFactory(layoutInflater) }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job

    open var useSharedViewModel: Boolean = false

    protected lateinit var viewModel: ViewModel
    protected abstract fun getViewModelClass(): Class<ViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job() // create the Job
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedInstanceState?.let { this.onSaveInstanceState(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeData()
        initCoroutineScope()
    }

    open fun initCoroutineScope() {
        launch {
            setupDatas()
        }
    }

    open suspend fun setupDatas() {

    }

    open fun setUpViews() {}

    open fun observeData() {}

    private fun init() {
        viewModel = if (useSharedViewModel) {
            ViewModelProvider(requireActivity()).get(
                getViewModelClass()
            )
        } else {
            ViewModelProvider(this).get(getViewModelClass())
        }
    }

    override fun onDestroy() {
        job.cancel() // cancel the Job
        super.onDestroy()
    }
}