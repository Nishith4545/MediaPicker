package com.nishith.justtestmodule

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.nishith.justtestmodule.databinding.ActivityMainBinding
import com.nishith.mediapicker.base.BaseActivity
import com.nishith.mediapicker.extention.loadImagefromServerAny
import com.nishith.mediapicker.fileselector.MediaSelectHelper
import com.nishith.mediapicker.fileselector.MediaSelector
import com.nishith.mediapicker.utils.FileHelperKit.getPath

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var mediaSelectHelper: MediaSelectHelper

    private var singlePhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaSelectHelper = MediaSelectHelper(this)
        singlePhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        setImagePicker()
        setClickListener()
    }

    override fun createViewBinding(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return  binding.root
    }

    private fun setClickListener() = with(binding) {
        btnLaunchPicker.setOnClickListener {
            mediaSelectHelper.canSelectMultipleImages(false)
            mediaSelectHelper.selectOptionsForImagePicker(true)

            //mediaSelectHelper.canSelectMultipleVideo(false)
            //mediaSelectHelper.selectOptionsForVideoPicker()
            //mediaSelectHelper.setLimitedAccessLayoutBackgroundColor(R.color.teal_200)
        }
    }

    private fun setImagePicker() = with(binding) {
        mediaSelectHelper.registerCallback(object : MediaSelector {
            override fun onVideoUri(uri: Uri) {
                super.onVideoUri(uri)
                getPath(this@MainActivity, uri)?.let { it1 ->
                    imageView.loadImagefromServerAny(it1)
                }
            }

            override fun onImageUri(uri: Uri) {
                uri.path?.let {
                    imageView.loadImagefromServerAny(it)
                }
            }

            override fun onCameraVideoUri(uri: Uri) {
                uri.path?.let {
                    imageView.loadImagefromServerAny(it)
                }
            }

            override fun onUpdatedStorageMedia(
                storageAccess: String,
                canSelectMultipleImages: Boolean,
                canSelectMultipleVideos: Boolean,
                selectFilter: String,
                mediaPath: String
            ) {
                super.onUpdatedStorageMedia(
                    storageAccess,
                    canSelectMultipleImages,
                    canSelectMultipleVideos,
                    selectFilter,
                    mediaPath
                )
                imageView.loadImagefromServerAny(
                    mediaPath
                )
            }
        }, supportFragmentManager)
    }

}
