package com.shopnest.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.shopnest.exception.OrderException;
import com.shopnest.exception.UserException;
import com.shopnest.modal.Order;
import com.shopnest.repository.OrderRepository;
import com.shopnest.response.ApiResponse;
import com.shopnest.response.PaymentLinkResponse;
import com.shopnest.service.OrderService;
import com.shopnest.service.UserService;
import com.shopnest.user.OrderStatus;
import com.shopnest.user.PaymentStatus;

@RestController
@RequestMapping("/api")
public class PaymentController {
	
	@Value("${razorpay.api.key}")
	String apiKey;
	@Value("${razorpay.api.secret}")
	String apiSecret;
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderRepository orderRepository;
	public PaymentController(OrderService orderService, UserService userService, OrderRepository orderRepository) {
		this.orderService = orderService;
		this.userService = userService;
		this.orderRepository = orderRepository;
	}
	
	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse>createPaymentLink(@PathVariable Long orderId, @RequestHeader("Authorization")String jwt)
	throws RazorpayException, UserException, OrderException{
		
		Order order = orderService.findOrderById(orderId);
		
		try {
			RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
			
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", order.getTotalDiscountedPrice()*100);
			paymentLinkRequest.put("currency", "INR");
			
			JSONObject notify = new JSONObject();
			notify.put("sms", true);
			notify.put("email", true);
			paymentLinkRequest.put("notify", notify);
			
			paymentLinkRequest.put("callback_url", "http://localhost:3000/payment/"+orderId);
			paymentLinkRequest.put("callback_method", "get");
			
			PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkId = payment.get("id");
			String paymentLinkUrl = payment.get("short_url");
			
			PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();
			paymentLinkResponse.setPaymentLinkId(paymentLinkId);
			paymentLinkResponse.setPaymentLinkUrl(paymentLinkUrl);
			
			return new ResponseEntity<PaymentLinkResponse>(paymentLinkResponse,HttpStatus.CREATED);
			
			
		} catch (Exception e) {
			throw new RazorpayException(e.getMessage());
		}
		
		
	}
	
	@GetMapping("/payments")
	public ResponseEntity<ApiResponse> redirect(@RequestParam(name="payment_id") String paymentId, @RequestParam(name = "order_id")Long orderId)
			throws OrderException, RazorpayException{
		RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
		Order order = orderService.findOrderById(orderId);
		
		try {
			Payment payment = razorpayClient.payments.fetch(paymentId);
			System.out.println("payment details --- "+payment+payment.get("status"));
			
			if(payment.get("status").equals("captured")) {
				order.getPaymentDetails().setPaymentId(paymentId);
				order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
				order.setOrderStatus(OrderStatus.PLACED);
				
				System.out.println(order.getPaymentDetails().getStatus()+"payment status ");
				orderRepository.save(order);
			}
			ApiResponse apiResponse = new ApiResponse("your oder got placed", true);
			return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
			
			
		} catch (Exception e) {
			System.out.println("error payment ------- ");
			throw new RazorpayException(e.getMessage());
		}
	}
	
	
	
	
}
