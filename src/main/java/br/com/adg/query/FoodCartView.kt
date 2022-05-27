package br.com.adg.query

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id

@Entity
data class FoodCartView(
	@Id val foodCartId: UUID,
	@ElementCollection(fetch = FetchType.EAGER) val products: MutableMap<UUID, Int>
) {
	constructor() : this(UUID.randomUUID(), HashMap<UUID, Int>()) {
	}

	fun addProducts(productId: UUID, amount: Int) =
		products.compute(productId) { _, quantity -> (quantity ?: 0) + amount}

	fun removeProducts(productId: UUID, amount: Int) {
		val leftOverQuantity = products.compute(productId) { _, quantity -> (quantity ?: 0) - amount }
		if (leftOverQuantity == 0) {
			products.remove(productId)
		}
	}
}

interface FoodCartViewRepository: JpaRepository<FoodCartView, UUID>