package com.app.crudapp.features.posts.presentation

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.crudapp.features.posts.domain.PostsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: PostsRepository
) : CoroutineWorker(context, workerParameters) {
    companion object {
        private const val TAG = "SyncWorker"
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            syncData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun syncData() {
        Timber.tag(TAG).d("syncData: from worker")
        val postsNeedSync = repository.getPostsFromLocalNeedsToSync()
        postsNeedSync.collect {
            Timber.tag(TAG).d(buildString {
                append("syncData: ")
                append(it.size)
                append(" needs to sync")
            })

            for (item in it) {
                if (item.isDeleted == true) {
                    Timber.tag(TAG).d("item need to delete")
                    val response = repository.deletePostRemote(item.id!!)

                    response.collectLatest {
                        repository.deletePostLocal(item)
                    }
                } else {
                    if (item.userId == 11) {//assume posts with user id ==11 are new posts
                        Timber.tag(TAG).d("item need to create")

                        val response = repository.addPostRemote(item)

                        response.collectLatest {
                            repository.updatePostLocalAsSynced(item)
                        }
                    } else {
                        Timber.tag(TAG).d("item need to update")
                        val response = repository.updatePostRemote(item)

                        response.collectLatest {
                            repository.updatePostLocalAsSynced(item)
                        }
                    }
                }

            }
        }
    }
}