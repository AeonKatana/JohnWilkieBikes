package com.johnwilkie.shop.paypal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PayPalConfig {

	
	@Bean
	public APIContext apiContext() throws PayPalRESTException{
		APIContext context = new APIContext("ASBeBnHuDFL_eSIdAs0koGy-78GmIfZQTWbdRkbALZKmRxJnNhmU42-JvzyIZCDykxkXcoex_gJeCCJz","ED8FW1VInxUrT2GsFSryWu4Ef5juiNc6tMlS_5EzTgjTzkITRKtQWUE_KHTsupdORVr-ANjzR1E-uikz", "sandbox");
		return context;
	}
	
	
}
