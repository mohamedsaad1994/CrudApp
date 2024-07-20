package com.app.crudapp.features.posts.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.crudapp.core.networkExeption.NetworkException
import com.app.crudapp.core.views.progressDialog.ProgressDialogImpl
import com.app.crudapp.databinding.ActivityMainBinding
import com.app.crudapp.features.posts.data.models.PostsListResponseItem
import com.app.crudapp.features.posts.presentation.PostsViewModel
import com.app.crudapp.features.posts.presentation.WorkerHelper.setupWorker
import com.app.crudapp.core.views.progressDialog.ProgressDialogHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PostsActivity : AppCompatActivity(), ProgressDialogHandler by ProgressDialogImpl(),
    PostsAdapter.PostAction {
    private lateinit var binding: ActivityMainBinding
    private val postsViewModel: PostsViewModel by viewModels()
    private lateinit var postsAdapter: PostsAdapter
    private var lastFetchedIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDialogWithLifecycleOwner(this)
        setupRecycler()
        observer()
        actions()
    }

    private fun setupRecycler() {
        postsAdapter = PostsAdapter(this)
        binding.rvPosts.adapter = postsAdapter
    }

    private fun observer() {
        postsViewModel.postsList.observe(this) { state ->
            run {
                changeDialogVisibility(state.isLoading)
                state.success?.let {
                    lifecycleScope.launch {
                        if (it.isEmpty())
                            binding.lytEmptyState.visibility = View.VISIBLE
                        else {
                            postsAdapter.addData(it, lastFetchedIndex, it.size)
                            binding.lytEmptyState.visibility = View.GONE
                        }
                    }
                }

                state.failed?.let {
                    binding.lytEmptyState.visibility = View.VISIBLE
                    when (it) {
                        is NetworkException.NetworkError -> {
                            val msg = it.error ?: "Empty"
                            Timber.tag(TAG).e("observer: %s", msg)
                        }

                        is NetworkException.ConnectionError -> {
                            Timber.tag(TAG).e("ConnectionError ${it.error}")
                        }

                        is NetworkException.GeneralException -> {
                            Timber.tag(TAG).e("GeneralException ${it.error}")
                        }

                        NetworkException.InvalidKey -> {
                            Timber.tag(TAG)
                                .e("observer:NetworkException.InvalidKey >> ${it.message}")
                        }
                    }
                }
            }
        }
        postsViewModel.addPost.observe(this) { state ->
            run {
                changeDialogVisibility(state.isLoading)
                state.success?.let {
                    lifecycleScope.launch {
                        Toast.makeText(
                            this@PostsActivity,
                            "Post sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                state.failed?.let {
                    when (it) {
                        is NetworkException.NetworkError -> {
                            val msg = it.error ?: "Empty"
                            Timber.tag(TAG).e("observer: %s", msg)
                        }

                        is NetworkException.ConnectionError -> {
                            Timber.tag(TAG).e("ConnectionError ${it.error}")
                        }

                        is NetworkException.GeneralException -> {
                            Timber.tag(TAG).e("GeneralException ${it.error}")
                        }

                        NetworkException.InvalidKey -> {
                            Timber.tag(TAG)
                                .e("observer:NetworkException.InvalidKey >> ${it.message}")
                        }
                    }
                }
            }
        }
        postsViewModel.updatePost.observe(this) { state ->
            run {
                changeDialogVisibility(state.isLoading)
                state.success?.let {
                    lifecycleScope.launch {
                        Toast.makeText(
                            this@PostsActivity,
                            "Post updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                state.failed?.let {
                    when (it) {
                        is NetworkException.NetworkError -> {
                            val msg = it.error ?: "Empty"
                            Timber.tag(TAG).e("observer: %s", msg)
                        }

                        is NetworkException.ConnectionError -> {
                            Timber.tag(TAG).e("ConnectionError ${it.error}")
                        }

                        is NetworkException.GeneralException -> {
                            Timber.tag(TAG).e("GeneralException ${it.error}")
                        }

                        NetworkException.InvalidKey -> {
                            Timber.tag(TAG)
                                .e("observer:NetworkException.InvalidKey >> ${it.message}")
                        }
                    }
                }
            }
        }
    }

    private fun actions() {
        binding.ivWritePost.setOnClickListener {
            val sheet = PostAction(PostActionState.ADD_NEW, onSubmit = { title, body ->
                postsViewModel.addPost(PostsListResponseItem(null, title, body, USER_ID))
            })
            sheet.isCancelable = true
            sheet.show(supportFragmentManager, PostAction.TAG)
        }
        binding.ivSync.setOnClickListener {
//            postsViewModel.syncProcess()
            setupWorker(this)
        }
    }

    override fun edit(data: PostsListResponseItem, adapterPosition: Int) {
        val sheet = PostAction(PostActionState.UPDATE_EXIST, data, onSubmit = { title, body ->
            postsViewModel.updatePost(PostsListResponseItem(data.id!!, title, body, data.userId))
            postsAdapter.updateItem(adapterPosition, title, body)
        })
        sheet.isCancelable = true
        sheet.show(supportFragmentManager, PostAction.TAG)
    }

    override fun delete(data: PostsListResponseItem, adapterPosition: Int) {
        val dialog = ConfirmDialogFragment(onConfirm = { isConfirmed ->
            if (isConfirmed) {
                postsViewModel.markPostAsDeleted(data)
                postsAdapter.deleteItem(adapterPosition)
            }
        })
        dialog.isCancelable = true
        dialog.show(supportFragmentManager, ConfirmDialogFragment.TAG)
    }

    companion object {
        private const val TAG = "PostsActivity"
        private const val USER_ID = 11 //assume my user id 11
    }
}