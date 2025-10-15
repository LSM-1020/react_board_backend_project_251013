package com.LSM.home.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SiteUserDto {

	@NotBlank(message = "ID를 입력해 주세요")
	@Size(min = 4,message = "ID는 최소 4글자 이상입니다" )
	private String username;
	
	@NotBlank(message = "PW를 입력해 주세요")
	@Size(min = 4,message = "PW는 최소 4글자 이상입니다" )
	private String password;
}
