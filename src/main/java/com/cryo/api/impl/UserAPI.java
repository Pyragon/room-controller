package com.cryo.api.impl;

import com.cryo.RoomController;
import com.cryo.api.AccountUtils;
import com.cryo.entities.Account;
import com.cryo.entities.annotations.Endpoint;
import com.cryo.entities.annotations.EndpointSubscriber;
import com.cryo.utils.ImageUtils;
import com.google.common.io.ByteStreams;
import com.google.common.net.MediaType;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;
import java.util.Random;

import static com.cryo.api.APIController.error;

@Slf4j
@EndpointSubscriber
public class UserAPI {

	private static final Random RANDOM = new Random();

	private static final String[] MESSAGES = { "Welcome back, %name%.", "How're ya now?", "Good, n'you, %name%?", "Get lit, %name%" };

	@Endpoint(method="POST", endpoint="/user")
	public static String getUserInfo(Request request, Response response) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Invalid token");
		Properties prop = new Properties();
		prop.put("displayName", account.getDisplayName());
		prop.put("imageName", account.getImageName());
		prop.put("message", MESSAGES[RANDOM.nextInt(MESSAGES.length)]);
		prop.put("success", true);
		return RoomController.getGson().toJson(prop);
	}

	@Endpoint(method="GET", endpoint="/user/avatar")
	public static String getAvatar(Request request, Response response) {
		Account account = AccountUtils.getAccount(request);
		if(account == null || account.getImageName().equals(""))
			return error404(response, "no account avatar");
		File file = new File("./data/images/"+account.getImageName());
		if(!file.exists())
			return error404(response, "file does not exist");
		try {
			BufferedImage image = ImageIO.read(file);
			if(image == null)
				return error404(response, "image read null");
			image = ImageUtils.getCircularImage(image);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "png", os);
			File test = new File("./data/images/test_"+account.getImageName());
			@Cleanup InputStream in = new ByteArrayInputStream(os.toByteArray());
			@Cleanup OutputStream testOut = new FileOutputStream(test);
			ImageIO.write(image, "png", testOut);
			@Cleanup OutputStream out = new BufferedOutputStream(response.raw().getOutputStream());
			response.raw().setContentType(MediaType.PNG.toString());
			response.status(200);
			ByteStreams.copy(in, out);
			out.flush();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error404(response, "end");
	}

	public static String error404(Response response, String error) {
		response.status(404);
		return error;
	}
}
