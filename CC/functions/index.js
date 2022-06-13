const functions = require("firebase-functions");
const express = require("express");
const cors = require("cors");

const admin = require("firebase-admin");
admin.initializeApp();

const app = express();
app.post("/register", async (req, res) => {
    const user = req.body;

    const email = user.email;

    const emailInput = await admin.firestore().collection('users').where("email","==",email).get();

    if (!emailInput.empty){
        res.status(200).send("Your email is already used!");
    } else {
        await admin.firestore().collection('users').doc(user.email).set(user);
        res.send(user);
    }
  });

app.post("/login", async (req, res) => {
    const user = req.body;

    const email = user.email;
    const password = user.password;

    const input = await admin.firestore().collection('users').where("email","==",email).where("password","==",password).get();

    if (!input.empty){
        const nameUser = await admin.firestore().collection('users').doc(user.email).get();
        //const input = JSON.stringify({name: nameUser.data().name, email: emailUser.data().email})
        
        res.send({ error: false, message: "success", loginResult:{name: nameUser.data().name, email: nameUser.data().email} });
    } else {
        res.send("Wrong email or password!");
    }
  });

exports.user = functions.https.onRequest(app);
