$(document).ready(usersFetch);

/*function dashboard_ticker() {
  setInterval(usersFetch, 8000);
}*/

async function usersFetch() {
  // add a new user onto the dashboard based on how many are returned
  const users = await (await fetch('/users')).json();
  const userDashboard = document.getElementById('profile-grid');
  userDashboard.innerHTML = '';
  console.log('here');
  users.forEach((user) => {
    console.log(user);
    userDashboard.appendChild(createUserCard(user));
    console.log('user checked');
  });
}

// create a user template
function createUserCard(user) {
  // make an empty card element
  const userCard = document.createElement('div');
  userCard.className = 'card homepage-card';

  // create the user banner
  const userBanner = document.createElement('div');
  userBanner.className = 'card-banner';

  // create the name banner
  const nameBanner = document.createElement('a');
  const name = document.createElement('h3');
  name.innerText = user.name;
  nameBanner.appendChild(name);

  // include the user profile photo
  const userIMG = document.createElement('img');
  userIMG.src = user.imgURL;
  userIMG.alt = 'assets/svgs/account.svg';
  userIMG.className = 'img-small';

  // create the teachlist categories
  const userTeachlist = document.createElement('div');
  userTeachlist.className = 'user-teachlist';

  user.teachlist.forEach((skill) => {
    const tagElement = document.createElement('div');
    tagElement.className = 'tag';
    tagElement.innerText = skill;
    userTeachlist.appendChild(tagElement);
  });

  // combine them in user card and return
  userBanner.appendChild(nameBanner);
  userBanner.appendChild(userIMG);
  userBanner.appendChild(userTeachlist);
  userCard.appendChild(userBanner);

  return userCard;
}
