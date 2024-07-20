package com.app.crudapp.features.posts.domain

import android.util.Log
import com.app.crudapp.core.networkExeption.ExceptionManger
import com.app.crudapp.features.posts.data.models.PostsListResponseItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.math.log

class PostsUseCase @Inject constructor(
    private val repository: PostsRepository,
) {

    suspend fun getPostsAndSaveItLocal(): Flow<ListPostsState> {
        return callbackFlow {
            try {
                val posts = repository.getPostsFromServer()

                posts.collectLatest {
                    trySend(
                        ListPostsState(success = it)
                    )
                    repository.savePostsInLocal(it)
                    channel.close()
                }
            } catch (e: Throwable) {
                trySend(
                    ListPostsState(failed = ExceptionManger.handleError(e))
                )
                channel.close()
            }
            awaitClose {
                channel.close()
            }

        }
    }

    suspend fun getPostsFromLocal(): Flow<ListPostsState> {
        return callbackFlow {
            try {
                val posts = repository.getPostsFromLocal()

                posts.collectLatest {
                    trySend(
                        ListPostsState(success = it)
                    )
                    channel.close()
                }
            } catch (e: Throwable) {
                trySend(
                    ListPostsState(failed = ExceptionManger.handleError(e))
                )
                channel.close()
            }
            awaitClose {
                channel.close()
            }

        }
    }

    suspend fun addPostRemote(item: PostsListResponseItem): Flow<ActionPostState> {
        return callbackFlow {
            try {
                val response = repository.addPostRemote(item)
                response.collectLatest {
                    trySend(
                        ActionPostState(success = it)
                    )
                    updateLocalPostAsSynced(item)
                    channel.close()
                }
            } catch (e: Throwable) {
                trySend(
                    ActionPostState(failed = ExceptionManger.handleError(e))
                )
                channel.close()
            }
            awaitClose {
                channel.close()
            }

        }
    }

    suspend fun addPostLocal(item: PostsListResponseItem): Flow<Long> {
        return callbackFlow {
            try {
                val response = repository.addPostLocal(item)

                response.collect {
                    trySend(
                        it
                    )
                    channel.close()
                }
            } catch (e: Throwable) {
                trySend(
                    0
                )
                channel.close()
            }
            awaitClose {
                channel.close()
            }

        }
    }

    private suspend fun updateLocalPostAsSynced(item: PostsListResponseItem) {
        repository.updatePostLocalAsSynced(item)
    }

    suspend fun updatePostRemote(
        item: PostsListResponseItem
    ): Flow<ActionPostState> {
        return callbackFlow {
            try {
                val response = repository.updatePostRemote(item = item)

                response.collectLatest {
                    trySend(
                        ActionPostState(success = it)
                    )
                    updateLocalPostAsSynced(it)
                    channel.close()
                }
            } catch (e: Throwable) {
                trySend(
                    ActionPostState(failed = ExceptionManger.handleError(e))
                )
                channel.close()
            }
            awaitClose {
                channel.close()
            }
        }
    }


    suspend fun updatePostLocal(item: PostsListResponseItem) {
        repository.updatePostLocal(item)
    }

    suspend fun markPostAsDeleted(item: PostsListResponseItem) {
        item.id?.let { repository.markPostAsDeleted(item) }
    }

    suspend fun deletePostRemote(item: PostsListResponseItem): Flow<ActionPostState> {
        return callbackFlow {
            try {
                val response = repository.deletePostRemote(item.id!!)

                response.collectLatest {
                    trySend(
                        ActionPostState(success = it)
                    )
                    channel.close()
                }
            } catch (e: Throwable) {
                trySend(
                    ActionPostState(failed = ExceptionManger.handleError(e))
                )
                channel.close()
            }
            awaitClose {
                channel.close()
            }
        }
    }

    suspend fun deletePostLocal(item: PostsListResponseItem) {
        repository.deletePostLocal(item)
    }

//    suspend fun getPostsFromLocalNeedsUpdate(): Flow<ListPostsState> {
//        return callbackFlow {
//            try {
//                val posts = repository.getPostsFromLocalNeedsUpdate()
//
//                posts.collectLatest {
//                    trySend(
//                        ListPostsState(success = it)
//                    )
//                    channel.close()
//                }
//            } catch (e: Throwable) {
//                trySend(
//                    ListPostsState(failed = ExceptionManger.handleError(e))
//                )
//                channel.close()
//            }
//            awaitClose {
//                channel.close()
//            }
//
//        }
//    }
//
//    suspend fun getPostsFromLocalNeedsCreate(): Flow<ListPostsState> {
//        return callbackFlow {
//            try {
//                val posts = repository.getPostsFromLocalNeedsCreate()
//
//                posts.collectLatest {
//                    trySend(
//                        ListPostsState(success = it)
//                    )
//                    channel.close()
//                }
//            } catch (e: Throwable) {
//                trySend(
//                    ListPostsState(failed = ExceptionManger.handleError(e))
//                )
//                channel.close()
//            }
//            awaitClose {
//                channel.close()
//            }
//
//        }
//    }
//
//    suspend fun getPostsFromLocalNeedsDelete(): Flow<ListPostsState> {
//        return callbackFlow {
//            try {
//                val posts = repository.getPostsFromLocalNeedsDelete()
//
//                posts.collectLatest {
//                    trySend(
//                        ListPostsState(success = it)
//                    )
//                    channel.close()
//                }
//            } catch (e: Throwable) {
//                trySend(
//                    ListPostsState(failed = ExceptionManger.handleError(e))
//                )
//                channel.close()
//            }
//            awaitClose {
//                channel.close()
//            }
//
//        }
//    }
companion object{
    private const val TAG = "PostsUseCase"
}
}