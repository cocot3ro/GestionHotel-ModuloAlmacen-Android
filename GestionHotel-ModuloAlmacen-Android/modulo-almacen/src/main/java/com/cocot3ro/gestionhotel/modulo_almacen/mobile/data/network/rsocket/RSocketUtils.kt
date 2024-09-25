package com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.network.rsocket

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.CompositeByteBuf
import io.rsocket.Payload
import io.rsocket.metadata.CompositeMetadataCodec
import io.rsocket.metadata.RoutingMetadata
import io.rsocket.metadata.TaggingMetadataCodec
import io.rsocket.metadata.WellKnownMimeType
import io.rsocket.util.DefaultPayload

object RSocketUtils {

    fun getPayload(route: String, data: String? = null): Payload {
        val dataBuf: ByteBuf = ByteBufAllocator.DEFAULT.buffer()

        if (data != null) {
            dataBuf.writeBytes(data.toByteArray())
        }

        val metadata: CompositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer()

        val routingMetadata: RoutingMetadata =
            TaggingMetadataCodec.createRoutingMetadata(ByteBufAllocator.DEFAULT, listOf(route))

        CompositeMetadataCodec.encodeAndAddMetadata(
            metadata,
            ByteBufAllocator.DEFAULT,
            WellKnownMimeType.MESSAGE_RSOCKET_ROUTING,
            routingMetadata.content
        )

        return DefaultPayload.create(dataBuf, metadata)
    }
}