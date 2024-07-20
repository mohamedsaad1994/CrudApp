package com.app.crudapp.features.posts.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.crudapp.features.posts.data.models.PostsListResponseItem
import com.app.crudapp.features.posts.domain.ActionPostState
import com.app.crudapp.features.posts.domain.ListPostsState
import com.app.crudapp.features.posts.domain.PostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsUseCase: PostsUseCase
) : ViewModel() {

    private val _postsList: MutableLiveData<ListPostsState> = MutableLiveData()
    val postsList: MutableLiveData<ListPostsState> get() = _postsList

    private val _addPost: MutableLiveData<ActionPostState> = MutableLiveData()
    val addPost: MutableLiveData<ActionPostState> get() = _addPost
    private val _updatePost: MutableLiveData<ActionPostState> = MutableLiveData()
    val updatePost: MutableLiveData<ActionPostState> get() = _updatePost

    init {
        getPostsAndSaveItLocal()
    }

    private fun getPostsAndSaveItLocal() {
        viewModelScope.launch {
            _postsList.value = ListPostsState(isLoading = true)

            val postsResponse =
                postsUseCase.getPostsAndSaveItLocal()

            postsResponse.collect {
                when {
                    it.success != null -> {
                        _postsList.value =
                            (ListPostsState(
                                isLoading = false,
                                success = it.success
                            ))
                    }

                    it.failed != null -> {
                        getPostsFromLocal()
                    }
                }
            }

        }
    }

    private fun getPostsFromLocal() {
        viewModelScope.launch {
            val postsResponse =
                postsUseCase.getPostsFromLocal()

            postsResponse.collect {
                when {
                    it.success != null -> {
                        _postsList.value =
                            (ListPostsState(
                                isLoading = false,
                                success = it.success
                            ))
                    }

                    it.failed != null -> {
                        _postsList.value = (ListPostsState(
                            isLoading = false,
                            failed = it.failed
                        ))
                    }
                }
            }

        }
    }


    private fun addPostRemote(item: PostsListResponseItem) {
        viewModelScope.launch {
            _addPost.value = ActionPostState(isLoading = true)

            val response =
                postsUseCase.addPostRemote(item)

            response.collect {
                when {
                    it.success != null -> {
                        _addPost.value =
                            (ActionPostState(
                                isLoading = false,
                                success = it.success
                            ))
                    }

                    it.failed != null -> {
                        _addPost.value = (ActionPostState(
                            isLoading = false,
                            failed = it.failed
                        ))
                    }
                }
            }

        }
    }


    fun addPost(item: PostsListResponseItem) {
        viewModelScope.launch {
            val response =
                postsUseCase.addPostLocal(item = item)

            response.collect {
                Timber.tag(TAG).d("addPostLocal id=? $it")
                if (it > 0) {
                    addPostRemote(
                        item = PostsListResponseItem(
                            it,
                            item.title,
                            item.body,
                            item.userId
                        )
                    )
                }
            }
        }
    }

    private fun updatePostRemote(item: PostsListResponseItem) {
        viewModelScope.launch {
            _updatePost.value = ActionPostState(isLoading = true)

            val response =
                postsUseCase.updatePostRemote(item)

            response.collect {
                when {
                    it.success != null -> {
                        _updatePost.value =
                            (ActionPostState(
                                isLoading = false,
                                success = it.success
                            ))
                    }

                    it.failed != null -> {
                        _updatePost.value = (ActionPostState(
                            isLoading = false,
                            failed = it.failed
                        ))
                    }
                }
            }

        }
    }

    fun updatePost(item: PostsListResponseItem) {
        viewModelScope.launch {
            postsUseCase.updatePostLocal(item)
            updatePostRemote(item)
        }
    }

    fun markPostAsDeleted(item: PostsListResponseItem) {
        viewModelScope.launch {
            postsUseCase.markPostAsDeleted(item)
            deletePostRemote(item)
        }
    }

    private fun deletePostLocal(item: PostsListResponseItem) {
        viewModelScope.launch {
            postsUseCase.deletePostLocal(item = item)
        }
    }

    private fun deletePostRemote(item: PostsListResponseItem) {
        viewModelScope.launch {
            val response =
                postsUseCase.deletePostRemote(item)

            response.collect {
                when {
                    it.success != null -> {
                        deletePostLocal(item)
                    }
                }
            }

        }

    }

//    fun syncProcess() {
//
//        viewModelScope.launch {
//            val postsToAdd = postsUseCase.getPostsFromLocalNeedsCreate()
//            val postsToUpdate = postsUseCase.getPostsFromLocalNeedsUpdate()
//            val postsToDelete = postsUseCase.getPostsFromLocalNeedsDelete()
//
//            postsToAdd.collect {
//                when {
//                    it.success != null -> {
//                        val list = it.success
//
//                        for (item in list) {
//                            addPostRemote(item)
//                        }
//                    }
//                }
//            }
//            postsToUpdate.collect {
//                when {
//                    it.success != null -> {
//                        val list = it.success
//                        for (item in list) {
//                            updatePostRemote(item)
//                        }
//                    }
//                }
//            }
//            postsToDelete.collect {
//                when {
//                    it.success != null -> {
//                        val list = it.success
//                        for (item in list) {
//                            deletePostRemote(item)
//                        }
//                    }
//                }
//            }
//        }
//    }

    companion object {
        private const val TAG = "PostsViewModel"
    }
}
