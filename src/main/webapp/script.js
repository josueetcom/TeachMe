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
  console.log('ID Token: ' + id_token);

  document.getElementById('user-profile').innerHTML = profile.getGivenName();
  document.getElementById('headert').innerHTML =
    profile.getGivenName() + ' ' + profile.getFamilyName();
  document.getElementById('email').innerHTML = profile.getEmail();
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
