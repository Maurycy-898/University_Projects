const mongoose = require('mongoose');
// schema
const NoteSchema = new mongoose.Schema({
  id: Number,
  title: String,
  body: String,
});

module.exports = mongoose.model('Note', NoteSchema);
