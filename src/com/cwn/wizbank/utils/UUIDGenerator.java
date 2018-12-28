package com.cwn.wizbank.utils;

import java.util.UUID;

public class UUIDGenerator {

	public static String generateUUID(){
		return UUID.randomUUID().toString();
	}
	
	
	public static void main(String[] args) {
		System.out.println(generateUUID());
	}
}
