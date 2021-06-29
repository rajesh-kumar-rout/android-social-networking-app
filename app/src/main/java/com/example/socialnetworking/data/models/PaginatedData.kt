package com.example.socialnetworking.data.models

class PaginatedData<T>(
    var networkStatus: NetworkStatus? = null,
    var data: MutableList<T> = mutableListOf(),
    var hasMore: Boolean = true,
    var errorMessage: String = String()
) {
    private fun copyWith(
        networkStatus: NetworkStatus? = this.networkStatus,
        data: MutableList<T> = this.data,
        hasMore: Boolean = this.hasMore,
        errorMessage: String = this.errorMessage
    ): PaginatedData<T>{
        return PaginatedData(
            networkStatus = networkStatus,
            data = data,
            hasMore = hasMore,
            errorMessage = errorMessage
        )
    }

    fun copyFrom(data: PaginatedData<T>){
        this.networkStatus = data.networkStatus
        this.hasMore = data.hasMore
        this.errorMessage = data.errorMessage
        this.data = data.data
    }

    fun stateLoading(): PaginatedData<T>{
        return copyWith(
            networkStatus = NetworkStatus.LOADING
        )
    }

    fun stateSuccess(data: MutableList<T>): PaginatedData<T>{
        this.data.addAll(data)
        return copyWith(
            networkStatus = NetworkStatus.SUCCESS,
            hasMore = data.isNotEmpty()
        )
    }

    fun stateError(exception: Exception): PaginatedData<T>{
        return copyWith(
            networkStatus = NetworkStatus.FAILURE,
            errorMessage = exception.message!!
        )
    }

    fun addAt(position: Int, data: T): PaginatedData<T>{
        this.data.add(position, data)
        return this
    }

    fun remove(data: T): PaginatedData<T>{
        this.data.remove(data)
        return this
    }

    fun isInitialLoading(): Boolean = data.isEmpty() && networkStatus == NetworkStatus.LOADING

    fun isInitialError(): Boolean = data.isEmpty() && networkStatus == NetworkStatus.FAILURE

    fun isEmpty(): Boolean = data.isEmpty() && networkStatus == NetworkStatus.SUCCESS

    fun shouldFetch(): Boolean = networkStatus != NetworkStatus.LOADING && hasMore
}