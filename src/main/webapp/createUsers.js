async function usersFetch() {
  //add a new user onto the dashboard based on how many are returned
  const users = await fetch('/users');
  const userDashboard = document.getElementById('profile-grid');
  console.log('here');
  users.forEach((user) => {
    userDashboard.appendChild(createUserCard(user));
    console.log('user checked');
  });
}

//create a user template
function createUserCard(user) {
  //make an empty card element
  const UserCard = document.createElement('div');
  UserCard.className = 'card';

  //create the user banner
  const UserBanner = document.createElement('div');
  UserBanner.className = 'card-banner';

  //create the name banner
  const nameBanner = document.createElement('a');
  const name = document.createElement('h3');
  name.innerText = user.name;
  nameBanner.appendChild(name);

  //create the teachlist categories
  const userTeachlist = document.createElement('div');
  userTeachlist.className = 'user-teachlist';

  const tagElement = document.createElement('div');
  tagElement.className = 'tag';
  tagElement.innerText = 'Tag';

  userTeachlist.appendChild(tagElement);
  userTeachlist.appendChild(tagElement);
  userTeachlist.appendChild(tagElement);

  //combine them in user card and return
  UserBanner.appendChild(nameBanner);
  UserBanner.appendChild(userTeachlist);
  UserCard.appendChild(UserBanner);

  return UserBanner;
}