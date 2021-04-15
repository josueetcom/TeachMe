package com.google.sps.servlets;

import java.io.FileReader;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import com.google.sps.data.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;

@WebServlet("/starter")
public class GoogleInfoServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (request.getHeader("X-Requested-With") == null) {
      // Without the `X-Requested-With` header, this request could be forged. Aborts.
    }

    // Set path to the Web application client_secret_*.json file you downloaded from
    // the
    // Google API Console: https://console.developers.google.com/apis/credentials
    // You can also find your Web application client ID and client secret from the
    // console and specify them directly when you create the
    // GoogleAuthorizationCodeTokenRequest
    // object.
    String CLIENT_SECRET_FILE = "/path/to/client_secret.json";

    // Exchange auth code for access token
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),
        new FileReader(CLIENT_SECRET_FILE));
    GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
        JacksonFactory.getDefaultInstance(), "https://oauth2.googleapis.com/token",
        clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(),
        request.getParameter("authCode"), "") // Specify the same redirect URI that you use with your web
                                              // app. If you don't have a web version of your app, you can
                                              // specify an empty string.
            .execute();

    // Get profile info from ID token
    GoogleIdToken idToken = tokenResponse.parseIdToken();
    GoogleIdToken.Payload payload = idToken.getPayload();
    String id = payload.getSubject(); // Use this value as a key to identify a user.
    String email = payload.getEmail();
    String name = (String) payload.get("name");
    String imgURL = (String) payload.get("picture");

    request.setAttribute("id", id);
    request.setAttribute("email", email);
    request.setAttribute("name", name);
    request.setAttribute("imgURL", imgURL);

    RequestDispatcher rd = request.getRequestDispatcher("/users");
    rd.forward(request, response);
  }
}