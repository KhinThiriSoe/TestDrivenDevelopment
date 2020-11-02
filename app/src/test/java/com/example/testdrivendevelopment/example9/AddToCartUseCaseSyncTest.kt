package com.example.testdrivendevelopment.example9

import com.example.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync
import com.example.testdrivendevelopment.example9.networking.CartItemScheme
import com.example.testdrivendevelopment.example9.networking.NetworkErrorException
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddToCartUseCaseSyncTest {

    private val OFFER_ID: String = "offerId"
    private val AMOUNT : Int = 4

    lateinit var SUT : AddToCartUseCaseSync

    // region helper fields
    @Mock
    lateinit var addToCartHttpEndpointSyncMock: AddToCartHttpEndpointSync

    @Before
    @Throws(Exception::class)
    fun setup(){
        SUT = AddToCartUseCaseSync(addToCartHttpEndpointSyncMock)
        success()
    }

    // correct parameters passed to the endpoint
    @Test
    @Throws(Exception::class)
    fun addToCartSync_correctParametersPassedToEndpoint() {
        // Arrange
        val ac: ArgumentCaptor<CartItemScheme> = ArgumentCaptor.forClass(CartItemScheme::class.java)
        // Art
        SUT.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        Mockito.verify(addToCartHttpEndpointSyncMock).addToCartSync(ac.capture())
        MatcherAssert.assertThat(ac.value.offerId, CoreMatchers.`is`(OFFER_ID))
        MatcherAssert.assertThat(ac.value.amount, CoreMatchers.`is`(AMOUNT))
    }

    // endpoint success - success returned
    @Test
    @Throws(Exception::class)
    fun addToCartSync_success_successReturned() {
        // Arrange
        // Art
        val result : AddToCartUseCaseSync.UseCaseResult = SUT.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        MatcherAssert.assertThat(
            result,
            CoreMatchers.`is`(AddToCartUseCaseSync.UseCaseResult.SUCCESS)
        )
    }

    // endpoint auth error - failure returned
    @Test
    @Throws(Exception::class)
    fun addToCartSync_authError_failureReturned() {
        // Arrange
        authError()
        // Art
        val result : AddToCartUseCaseSync.UseCaseResult = SUT.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        MatcherAssert.assertThat(
            result,
            CoreMatchers.`is`(AddToCartUseCaseSync.UseCaseResult.FAILURE)
        )
    }

    // endpoint general error - failure returned
    @Test
    @Throws(Exception::class)
    fun addToCartSync_generalError_failureReturned() {
        // Arrange
        generalError()
        // Art
        val result : AddToCartUseCaseSync.UseCaseResult = SUT.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        MatcherAssert.assertThat(
            result,
            CoreMatchers.`is`(AddToCartUseCaseSync.UseCaseResult.FAILURE)
        )
    }

    // network exception - network error returned
    @Test
    @Throws(Exception::class)
    fun addToCartSync_networkError_networkErrorReturned() {
        // Arrange
        networkError()
        // Art
        val result : AddToCartUseCaseSync.UseCaseResult = SUT.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        MatcherAssert.assertThat(
            result,
            CoreMatchers.`is`(AddToCartUseCaseSync.UseCaseResult.NETWORK_ERROR)
        )
    }

    // region helper methods
    @Throws(NetworkErrorException::class)
    private fun authError() {
        Mockito.`when`(addToCartHttpEndpointSyncMock.addToCartSync(Mockito.any(CartItemScheme::class.java)))
            .thenReturn(
            AddToCartHttpEndpointSync.EndpointResult.AUTH_ERROR
        )
    }

    @Throws(NetworkErrorException::class)
    private fun generalError() {
        Mockito.`when`(addToCartHttpEndpointSyncMock.addToCartSync(Mockito.any(CartItemScheme::class.java)))
            .thenReturn(
            AddToCartHttpEndpointSync.EndpointResult.GENERAL_ERROR
        )
    }

    @Throws(NetworkErrorException::class)
    private fun networkError() {
        Mockito.doThrow(NetworkErrorException())
            .`when`(
                addToCartHttpEndpointSyncMock).addToCartSync(
                Mockito.any(CartItemScheme::class.java)
            )
    }

    @Throws(NetworkErrorException::class)
    private fun success() {
        Mockito.`when`(addToCartHttpEndpointSyncMock.addToCartSync(Mockito.any(CartItemScheme::class.java)))
            .thenReturn(
                AddToCartHttpEndpointSync.EndpointResult.SUCCESS
            )
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}