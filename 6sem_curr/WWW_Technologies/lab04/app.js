const dotenv = require('dotenv');
const methodOverride = require('method-override')
const express = require('express');
const expressLayouts = require('express-ejs-layouts');
const mongoose = require('mongoose');

const app = express();
const port = 3000 || process.env.PORT;

app.use(express.json());
app.use(express.urlencoded({extended: true}));
app.use(methodOverride('_method'))

// Connect to database
mongoose.connect('mongodb://127.0.0.1:27017/notes_db')
  .then(() => {
    console.log('database connection success');
  }).catch(error => {
    console.error('database connection error', error);
  });

// Static files
app.use(express.static('public'));

// Template engine
app.use(express.json());
app.use(expressLayouts);
app.set('layout', './layouts/home');
app.set('view engine', 'ejs');

// Routes
app.use('/', require('./server/routes/index'));

// Handle 404
app.get('*', function(req, res) {
  res.status(404).render('404');
})

// start app
app.listen(port, () => {
  console.log(`App listening on port: http://localhost:${port}`);
});
