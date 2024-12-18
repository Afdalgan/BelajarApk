package com.dicoding.picodiploma.loginwithanimation.view.story

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService

class StoriesPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getStories(page, params.loadSize)

            LoadResult.Page(
                data = response.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.listStory.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}