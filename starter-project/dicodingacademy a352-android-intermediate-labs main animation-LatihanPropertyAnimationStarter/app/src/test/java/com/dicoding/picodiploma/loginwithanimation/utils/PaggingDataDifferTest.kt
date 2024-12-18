package com.dicoding.picodiploma.loginwithanimation.utils
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers

class PagingDataDifferTest<T : Any> {

    private val differ = AsyncPagingDataDiffer(
        diffCallback = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
        },
        updateCallback = NoopListUpdateCallback(),
        workerDispatcher = Dispatchers.Main.immediate
    )

    suspend fun submitData(pagingData: PagingData<T>) {
        differ.submitData(pagingData)
    }

    fun snapshot() = differ.snapshot()
}


class NoopListUpdateCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}


