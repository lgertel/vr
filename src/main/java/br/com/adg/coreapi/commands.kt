package br.com.adg.coreapi

import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

class CreateFoodCartCommand(
	@RoutingKey val foodCartId: UUID
)

data class SelectProductCommand(
	@TargetAggregateIdentifier val foodCartId: UUID,
	val productId: UUID,
	val quantity: Int
)

data class DeselectProductCommand(
	@TargetAggregateIdentifier val foodCartId: UUID,
	val productId: UUID,
	val quantity: Int
)

data class ConfirmOrderCommand(
	@TargetAggregateIdentifier val foodCartId: UUID
)