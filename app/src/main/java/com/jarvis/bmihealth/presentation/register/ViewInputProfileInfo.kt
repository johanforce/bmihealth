package com.jarvis.bmihealth.presentation.register

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ViewInputProfileInfoBinding
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.utilx.AddWeightUtil
import com.jarvis.bmihealth.presentation.utilx.DeviceUtil
import com.jarvis.bmihealth.presentation.utilx.FragmentUtils
import com.jarvis.bmihealth.presentation.utilx.click
import com.jarvis.bmihealth.presentation.utilx.cropimage.PermissionConst
import com.jarvis.bmihealth.presentation.utilx.cropimage.UCropImagePresenter
import com.jarvis.design_system.bottomsheet.BottomSheetEventListener
import com.jarvis.design_system.bottomsheet.JxBottomSheet
import com.jarvis.design_system.bottomsheet.JxBottomSheetConfiguration
import com.jarvis.design_system.bottomsheet.JxBottomSheetItemData
import com.jarvis.design_system.forminput.OnStateChangedListener
import com.yalantis.ucrop.UCrop
import java.io.InputStream
import java.util.*

@Suppress("UNUSED_PARAMETER", "unused", "MemberVisibilityCanBePrivate")
class ViewInputProfileInfo : ConstraintLayout {
    var byteArray: ByteArray? = null
    private var binding: ViewInputProfileInfoBinding? = null
    var isChangName = false
    private var onClick: OnClickListener? = null
    private var bottomSheetChooseImage: JxBottomSheet? = null
    private var uCropImagePresenter: UCropImagePresenter? = null
    private var isPermissionCamera = false
    private var isPermissionStorage = false
    var currentPhotoPath: String? = null
    var myPath = ""


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun initDialogChooseImage() {
        val title: String = context.getString(R.string.addweight_choose_photo_title)
        val cameraItemTitle: String = context.getString(R.string.all_came)
        val galleryItemTitle: String = context.getString(R.string.all_gallery)
        val listData: MutableList<JxBottomSheetItemData> = LinkedList()
        listData.add(JxBottomSheetItemData(R.drawable.all_ic_camera, cameraItemTitle))
        listData.add(JxBottomSheetItemData(R.drawable.all_ic_image, galleryItemTitle))
        val configuration = JxBottomSheetConfiguration(title)
            .setListItemStyle(JxBottomSheetConfiguration.LIST_ITEM_STYLE_GRID)
            .setListData(listData)
        this.bottomSheetChooseImage = JxBottomSheet(configuration)
        this.bottomSheetChooseImage!!.setEventListener(object : BottomSheetEventListener() {
            override fun onItemClick(position: Int) {
                super.onItemClick(position)
                when (position) {
                    0 -> onClickChooseCamera()
                    1 -> onClickChooseGallery()
                }
            }
        })
        FragmentUtils.showDialogFragment(
            context as AppCompatActivity,
            bottomSheetChooseImage,
            "select_image_profile"
        )
    }

    fun setEnableErrorStateInputField(isEnable: Boolean) {
        binding?.edFirstName?.setEnableErrorState(isEnable)
        binding?.edLastName?.setEnableErrorState(isEnable)
        binding?.edBio?.setEnableErrorState(isEnable)
    }

    fun setVisibleAvatar(isVisible: Boolean) {
        if (isVisible) {
            binding?.avatarView?.visibility = VISIBLE
            binding?.ivCamera?.visibility = VISIBLE
        } else {
            binding?.avatarView?.visibility = GONE
            binding?.ivCamera?.visibility = GONE
        }
    }

    fun onClickChooseCamera() {
        ActivityCompat.requestPermissions(
            context as Activity,
            PermissionConst.REQUIRED_PERMISSIONS_CAMERA,
            PermissionConst.REQUEST_CODE_PERMISSIONS
        )
    }

    fun onClickChooseGallery() {
        ActivityCompat.requestPermissions(
            context as Activity,
            PermissionConst.REQUIRED_PERMISSIONS_STORAGE,
            PermissionConst.REQUEST_CODE_PERMISSIONS_STORAGE
        )
    }

    fun setFirstName(name: String) {
        this.binding?.edFirstName?.setValue(name)
        this.binding?.edFirstName?.setSelection()
    }

    fun setLastName(name: String) {
        this.binding?.edLastName?.setValue(name)
        this.binding?.edLastName?.setSelection()
    }

    fun setBio(name: String) {
        this.binding?.edBio?.setValue(name)
        this.binding?.edBio?.setSelection()
    }


    fun clearFocus(name: String) {
        this.binding?.edLastName?.clearFocus()
        this.binding?.edFirstName?.clearFocus()
        this.binding?.edBio?.clearFocus()
    }

    fun clickFirstName() {
        this.binding?.edFirstName?.inputField?.requestFocus()
        this.binding?.edFirstName?.setValue("")
    }

    fun clickBio() {
        this.binding?.edBio?.inputField?.requestFocus()
        this.binding?.edBio?.setValue("")
    }

    fun clickLastName() {
        this.binding?.edLastName?.inputField?.requestFocus()
        this.binding?.edLastName?.setValue("")
    }

    fun setAvatar(name: String, avatarUrl: String?, byteArray: ByteArray?) {
        this.binding?.avatarView?.setDataAvatar(false, name, avatarUrl, byteArray)
    }

    fun setUserProfile(userProfile: ProfileUserModel?) {
        userProfile ?: return
        byteArray = userProfile.avatar
        setFirstName(userProfile.firstname)
        setLastName(userProfile.lastname)
        setBio(userProfile.bio)
        setAvatar(
            userProfile.firstname + " " + userProfile.lastname,
            userProfile.avatarUrl,
            userProfile.avatar
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClick = onClickListener
    }

    private fun init() {
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewInputProfileInfoBinding.inflate(inflate, this, true)

        binding?.edFirstName?.inputField?.setOnStateChangedListener(object :
            OnStateChangedListener {
            override fun onTyping() {
                //do nothing
            }

            override fun onNormal() {
                //do nothing
            }

            override fun onError() {
                //do nothing
            }
        })

        binding?.edLastName?.inputField?.setOnStateChangedListener(object : OnStateChangedListener {
            override fun onTyping() {  //do nothing
            }

            override fun onNormal() {  //do nothing
            }

            override fun onError() {  //do nothing
            }
        })

        binding?.edBio?.inputField?.setOnEditorActionListener { _, p1, _ ->
            if (p1 == EditorInfo.IME_ACTION_DONE) {
                binding?.edBio?.inputField?.clearFocusWellText()
                binding?.edBio?.inputField?.onNormalState()
                //showError()
            }
            false
        }

        binding?.edLastName?.inputField?.setOnEditorActionListener { _, p1, _ ->
            if (p1 == EditorInfo.IME_ACTION_NEXT) {
                binding?.edLastName?.inputField?.clearFocusWellText()
                binding?.edLastName?.inputField?.onNormalState()
                //showError()
            }
            false
        }

        binding?.edFirstName?.inputField?.setOnEditorActionListener { _, p1, _ ->
            if (p1 == EditorInfo.IME_ACTION_NEXT) {
                binding?.edFirstName?.inputField?.clearFocusWellText()
                binding?.edFirstName?.inputField?.onNormalState()
//                showError()
            }
            false
        }
        binding?.root?.setOnClickListener {
            clearFocusView()
        }

        binding?.avatarView?.click {
            onClick?.onChooseAvatar()
        }
        this.uCropImagePresenter = UCropImagePresenter(context as AppCompatActivity)

        clearFocusView()
    }

    fun getFirstName(): String? {
        if (binding?.edFirstName?.inputField?.content?.trim() == "" && binding?.edLastName?.inputField?.content?.trim() == "") {
            return "Health"
        }
        return binding?.edFirstName?.inputField?.content
    }

    fun getBio(): String {
        return binding?.edBio?.inputField?.content ?: "Bio"
    }

    fun getLastName(): String? {
        if (binding?.edFirstName?.inputField?.content?.trim() == "" && binding?.edLastName?.inputField?.content?.trim() == "") {
            return "for you"
        }
        return binding?.edLastName?.inputField?.content
    }

    fun getFirstNameBMIOther(): String? {
        if (binding?.edFirstName?.inputField?.content?.trim() == "" && binding?.edLastName?.inputField?.content?.trim() == "") {
            return "My"
        }
        return binding?.edFirstName?.inputField?.content
    }

    fun getLastNameBMIOther(): String? {
        if (binding?.edFirstName?.inputField?.content?.trim() == "" && binding?.edLastName?.inputField?.content?.trim() == "") {
            return "Friend"
        }
        return binding?.edLastName?.inputField?.content
    }


    fun isValidInput(): Boolean {
        if (binding?.edFirstName?.inputField?.content?.trim() == "" && binding?.edLastName?.inputField?.content?.trim() == "") {
            return true
        }
        if (binding?.edFirstName?.inputField?.isContentValid == false || binding?.edLastName?.inputField?.isContentValid == false) {
            if (binding?.edFirstName?.inputField?.isContentValid == false) {
                binding?.edFirstName?.inputField?.onErrorState()
            }

            if (binding?.edLastName?.inputField?.isContentValid == false) {
                binding?.edLastName?.inputField?.onErrorState()
            }
            return false
        }
        return true
    }

    fun isValidInputBMIOther(): Boolean {
        return if (binding?.edFirstName?.inputField?.content?.trim() == "" && binding?.edLastName?.inputField?.content?.trim() == "") {
            true
        } else binding?.edFirstName?.inputField?.content?.trim() != "" && binding?.edLastName?.inputField?.content?.trim() != ""
    }

    fun setHintFirstName(data: String) {
        this.binding?.edFirstName?.setHintValue(data)
    }

    fun setHintLastName(data: String) {
        this.binding?.edLastName?.setHintValue(data)
    }

    fun setHintBio(data: String) {
        this.binding?.edBio?.setHintValue(data)
    }
//    private fun showError() {
//        binding?.edFirstName?.inputField?.isContentValid
//        binding?.edFirstName?.inputField?.clearFocusWellText()
//        binding?.edLastName?.inputField?.isContentValid
//        binding?.edLastName?.inputField?.clearFocusWellText()
//    }

    fun clearFocusView() {

        checkHideKeyBroadWhenTouchClick()
        binding?.edFirstName?.inputField?.clearFocusWellText()
        binding?.edFirstName?.inputField?.clearFocus()
        binding?.edFirstName?.inputField?.onNormalState()
        binding?.edBio?.inputField?.clearFocusWellText()
        binding?.edBio?.inputField?.clearFocus()
        binding?.edBio?.inputField?.onNormalState()
        binding?.edLastName?.inputField?.clearFocusWellText()
        binding?.edLastName?.inputField?.clearFocus()
        binding?.edLastName?.inputField?.onNormalState()
        binding?.edFirstName?.clearFocus()
        binding?.edBio?.clearFocus()
        binding?.edLastName?.clearFocus()
//            showError()

    }

    fun clearFocusViewInit() {
        checkHideKeyBroadWhenTouchClick()
        binding?.edFirstName?.inputField?.clearFocusWellText()
        binding?.edFirstName?.inputField?.clearFocus()
        binding?.edFirstName?.inputField?.onNormalState()
        binding?.edBio?.inputField?.clearFocusWellText()
        binding?.edBio?.inputField?.clearFocus()
        binding?.edBio?.inputField?.onNormalState()
        binding?.edLastName?.inputField?.clearFocusWellText()
        binding?.edLastName?.inputField?.clearFocus()
        binding?.edLastName?.inputField?.onNormalState()
        binding?.edFirstName?.clearFocus()
        binding?.edBio?.clearFocus()
        binding?.edLastName?.clearFocus()

    }

    fun executeOnPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        for (i in permissions.indices) {
            if (Manifest.permission.CAMERA == permissions[i] && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                uCropImagePresenter?.let { this.requestPermissionView(context as Activity, it) }
            }
            if (Manifest.permission.READ_EXTERNAL_STORAGE == permissions[i] && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                this.uCropImagePresenter?.let {
                    requestPermissionStorageView(
                        context as Activity,
                        it
                    )
                }
            }
        }
    }

    fun requestPermissionView(context: Activity, uCropImagePresenter: UCropImagePresenter) {
        checkPermissionsGranted(context)
        if (this.isPermissionCamera) {
            this.currentPhotoPath = uCropImagePresenter.takePictureIntent(context)
        } else {
            ActivityCompat.requestPermissions(
                context,
                PermissionConst.REQUIRED_PERMISSIONS_CAMERA,
                PermissionConst.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    fun requestPermissionStorageView(context: Activity, uCropImagePresenter: UCropImagePresenter) {
        this.checkPermissionsGranted(context)
        if (this.isPermissionStorage) {
            uCropImagePresenter.chooseFromGallery(context)
        } else {
            ActivityCompat.requestPermissions(
                context,
                PermissionConst.REQUIRED_PERMISSIONS_STORAGE,
                PermissionConst.REQUEST_CODE_PERMISSIONS_STORAGE
            )
        }
    }

    private fun checkPermissionsGranted(context: Activity) {
        if (ContextCompat.checkSelfPermission(
                context,
                "android.permission.CAMERA"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            this.isPermissionCamera = true
        }
        if (ContextCompat.checkSelfPermission(
                context,
                "android.permission.READ_EXTERNAL_STORAGE"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            this.isPermissionStorage = true
        }

    }

    private fun checkHideKeyBroadWhenTouchClick() {
        DeviceUtil.hideKeyboard(context as AppCompatActivity)
    }

    fun executeOnActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        contentResolver: ContentResolver,
    ): ByteArray? {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                UCropImagePresenter.REQUEST_TAKE_PHOTO -> {
                    val uri = uCropImagePresenter!!.galleryAddPic(context, this.currentPhotoPath)
                    if (uri != null) {
                        uCropImagePresenter!!.startCrop(uri)
                    }
                }
                UCropImagePresenter.REQUEST_GET_SINGLE_FILE -> {
                    val uroImage = data!!.data
                    if (uroImage != null) {
                        uCropImagePresenter!!.startCrop(uroImage)
                    }
                }
                UCrop.REQUEST_CROP -> {
                    val imageUri: Uri = data?.let { UCrop.getOutput(it) } ?: return null
                    val iStream: InputStream = contentResolver.openInputStream(imageUri)!!
                    val byteArray = AddWeightUtil.getBytes(iStream)
                    binding!!.avatarView.setDataAvatar(false, "", null, byteArray)
                    this.byteArray = byteArray
                    return byteArray
                }
            }
        }
        return null
    }

    interface OnClickListener {
        fun onChooseAvatar()
    }
}
