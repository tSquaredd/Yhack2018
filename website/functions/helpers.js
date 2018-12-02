module.exports = {

    getUserOptions: (user) => {
        return {
            name: user.name,
            email: user.email,
            pro: user.pro
        };
    }

}