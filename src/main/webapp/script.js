var counter = 0;
function onSignIn(googleUser) {
  // Useful data for your client-side scripts:
  var profile = googleUser.getBasicProfile();
  console.log('ID: ' + profile.getId()); // Don't send this directly to your server!
  console.log('Full Name: ' + profile.getName());
  console.log('Given Name: ' + profile.getGivenName());
  console.log('Family Name: ' + profile.getFamilyName());
  console.log('Image URL: ' + profile.getImageUrl());
  console.log('Email: ' + profile.getEmail());

  // The ID token you need to pass to your backend:
  var id_token = googleUser.getAuthResponse().id_token;
  //   console.log('ID Token: ' + id_token);
  document.getElementById('menuUsername').innerHTML = profile.getGivenName();
  document.getElementById('profileTitle').innerHTML =
    'Welcome ' + profile.getName();
  document.getElementById('profileEmail').innerHTML = profile.getEmail();
  //   document.getElementById('profileId').value = profile.getId();

  signInCallback(id_token);
}

function signInCallback(authResult) {
  if (authResult['code']) {
    // Send the code to the server
    $.ajax({
      type: 'POST',
      url: '/starter',
      // Always include an `X-Requested-With` header in every AJAX request,
      // to protect against CSRF attacks.
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
      },
      contentType: 'application/octet-stream; charset=utf-8',
      success: function (result) {
        console.log('success');
      },
      processData: false,
      data: authResult['code'],
    });
  } else {
    console.log('failure');
  }
}

function toggleContent() {
  if (counter == 0) {
    document.getElementById('content-userprofile').style.display = 'flex';
    document.getElementById('content-dashboard').style.display = 'none';
    counter += 1;
  } else {
    document.getElementById('content-userprofile').style.display = 'none';
    document.getElementById('content-dashboard').style.display = '';
    counter = 0;
  }
}

/** Fetches the user's chats from the server and adds it to the page. */
async function loadChats() {
  console.log('hello');
  // const userId = googleUser.getBasicProfile().getId();
  userId = '104737820262548186234';
  const matchContainer = document.getElementById('matchList');
  const responseFromServer = await fetch('/chats?userid=' + userId);
  const chatsJson = await responseFromServer.json();

  console.log(JSON.stringify(chatsJson));

  // for (chat in chatsJson) {
  //     console.log(chat.id);
  // }
}
