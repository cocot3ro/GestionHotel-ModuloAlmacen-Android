package com.cocot3ro.gestionhotel.modulo_almacen.mobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rsocket.core.RSocketConnector
import io.rsocket.frame.decoder.PayloadDecoder
import io.rsocket.metadata.WellKnownMimeType
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RSocketModule {

    @Provides
    @Singleton
    fun provideRSocket(): RSocketConnector {
        return RSocketConnector.create()
            .metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.string)
            .dataMimeType(WellKnownMimeType.APPLICATION_JSON.string)
            .payloadDecoder(PayloadDecoder.ZERO_COPY)
    }
}