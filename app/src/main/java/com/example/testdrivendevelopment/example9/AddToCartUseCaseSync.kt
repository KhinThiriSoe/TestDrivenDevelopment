package com.example.testdrivendevelopment.example9

import com.example.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync
import com.example.testdrivendevelopment.example9.networking.CartItemScheme
import com.example.testdrivendevelopment.example9.networking.NetworkErrorException

class AddToCartUseCaseSync(addToCartHttpEndpointSync: AddToCartHttpEndpointSync) {

    enum class UseCaseResult {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    private val mAddToCartHttpEndpointSync: AddToCartHttpEndpointSync = addToCartHttpEndpointSync

    fun addToCartSync(offerId: String, amount: Int): UseCaseResult {
        val result: AddToCartHttpEndpointSync.EndpointResult? = try {
            mAddToCartHttpEndpointSync.addToCartSync(CartItemScheme(offerId, amount))
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }
        return when (result) {
            AddToCartHttpEndpointSync.EndpointResult.SUCCESS -> UseCaseResult.SUCCESS
            AddToCartHttpEndpointSync.EndpointResult.AUTH_ERROR,
            AddToCartHttpEndpointSync.EndpointResult.GENERAL_ERROR -> UseCaseResult.FAILURE
            else -> throw RuntimeException("invalid endpoint result: $result")
        }
    }

}