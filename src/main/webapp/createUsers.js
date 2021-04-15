$(document).ready(usersFetch);

/*function dashboard_ticker() {
  setInterval(usersFetch, 8000);
}*/

async function usersFetch() {
  // add a new user onto the dashboard based on how many are returned
  const users = await (await fetch('/users')).json();
  const userDashboard = document.getElementById('grid');
  // also clear the dashboard before adding new users
  const sCards = Array.from(document.getElementsByClassName('small-card'));
  sCards.forEach(function (card) {
    card.remove();
  });
  const mCards = Array.from(document.getElementsByClassName('medium-card'));
  mCards.forEach(function (card) {
    card.remove();
  });
  users.forEach((user) => {
    userDashboard.appendChild(createUserCard(user));
  });
}

// create a user template
function createUserCard(user) {
  // make an empty card element
  const userCard = document.createElement('div');
  userCard.className = 'card medium-card';

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
    tagElement.className = 'item tag';
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
