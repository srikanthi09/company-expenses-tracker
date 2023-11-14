package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.PaymentMode;

public interface PaymentModeRepo extends JpaRepository<PaymentMode, String> {

	void deleteById(String paymentCode);

	PaymentMode findByPaymentName(String paymentName);

	PaymentMode findByPaymentCode(String paymentCode);
}
