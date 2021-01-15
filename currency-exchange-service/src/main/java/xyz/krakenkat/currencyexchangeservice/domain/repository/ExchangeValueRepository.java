package xyz.krakenkat.currencyexchangeservice.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.krakenkat.currencyexchangeservice.domain.model.ExchangeValue;

public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long> {
    ExchangeValue findByFromAndTo(String from, String to);
}
