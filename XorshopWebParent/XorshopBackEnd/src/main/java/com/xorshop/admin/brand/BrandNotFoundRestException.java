package com.xorshop.admin.brand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND, reason="Marque Introuvable")
public class BrandNotFoundRestException extends Exception {

}
