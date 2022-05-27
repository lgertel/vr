package br.com.adg.command;

import br.com.adg.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Aggregate
public class FoodCart {

  private  static final Logger logger = LoggerFactory.getLogger(FoodCart.class);

  @AggregateIdentifier
  private UUID foodCartId;
  private Map<UUID, Integer> selectedProducts;
  private  boolean confirmed;

  @CommandHandler
  public FoodCart(CreateFoodCartCommand command) {
    AggregateLifecycle.apply(new FoodCartCreatedEvent(command.getFoodCartId()));
  }

  @CommandHandler
  public void handle(SelectProductCommand command) {
    AggregateLifecycle.apply(new ProductSelectedEvent(foodCartId, command.getProductId(), command.getQuantity()));
  }

  @CommandHandler
  public void handle(DeselectProductCommand command) {
    AggregateLifecycle.apply(new ProductDeselectedEvent(foodCartId, command.getProductId(), command.getQuantity()));
  }

  @CommandHandler
  public void handle(ConfirmOrderCommand command) {
    AggregateLifecycle.apply(new OrderConfirmedEvent(foodCartId));
  }

  @EventSourcingHandler
  public void on(FoodCartCreatedEvent event) {
    foodCartId = event.getFoodCartId();
    selectedProducts = new HashMap<>();
    confirmed = false;
  }

  @EventSourcingHandler
  public void on(ProductSelectedEvent event) {
    selectedProducts.merge(event.getProductId(), event.getQuantity(), Integer::sum);
  }

  @EventSourcingHandler
  public void on(ProductDeselectedEvent event) {
    selectedProducts.computeIfPresent(
        event.getProductId(),
        (productId, quantity) -> quantity -= event.getQuantity()
    );
  }

  @EventSourcingHandler
  public void on (OrderConfirmedEvent event) {
    confirmed = true;
  }
  public FoodCart() {}
}
