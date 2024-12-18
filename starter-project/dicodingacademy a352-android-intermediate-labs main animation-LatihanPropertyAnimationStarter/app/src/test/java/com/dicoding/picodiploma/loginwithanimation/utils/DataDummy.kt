package com.dicoding.picodiploma.loginwithanimation.utils

import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import androidx.paging.PagingData
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesResponse

object DataDummy {
    fun generateDummyStories(): PagingData<StoryItem> {
        val listStory = mutableListOf<StoryItem>()
        for (i in 1..10) {
            listStory.add(
                StoryItem(
                    id = "story-$i",
                    name = "User $i",
                    description = "Description $i",
                    photoUrl = "https://example.com/photo$i.jpg",
                    createdAt = "2024-06-01T12:00:00Z",
                    lat = null,
                    lon = null
                )
            )
        }
        return PagingData.from(listStory)
    }

    fun generateEmptyStories(): PagingData<StoryItem> {
        return PagingData.from(emptyList())
    }

    fun generateDummyStoriesResponse(): StoriesResponse {
        val listStory = mutableListOf<StoryItem>()
        for (i in 1..10) {
            listStory.add(
                StoryItem(
                    id = "story-$i",
                    name = "User $i",
                    description = "Description $i",
                    photoUrl = "https://example.com/photo$i.jpg",
                    createdAt = "2024-06-01T12:00:00Z",
                    lat = null,
                    lon = null
                )
            )
        }
        return StoriesResponse(
            error = false,
            message = "Success",
            listStory = listStory
        )
    }


    fun generateEmptyStoriesResponse(): StoriesResponse {
        return StoriesResponse(
            error = false,
            message = "No stories",
            listStory = emptyList()
        )
    }
}
