const Note = require("../models/Notes.js");


/**
 * GET 
 * Home page
 */
exports.homepage = async (req, res) => {
  const locals = {
    title: "Notes App",
    description: "Simple app to take and share your notes."
  };

  res.render('home', {
    locals,
    layout: '../views/layouts/home'
  });
};

/**
 * GET
 * Notes page
 */
exports.notespage = async (req, res) => {
  
  const locals = {
    title: "Notes",
    description: "Simple app to take and share your notes.",
  };

  Note.find()
    .then(notes => {
      res.render('notes/index', { 
        locals,
        notes,
        layout: "../views/layouts/notes"
      });
    })
    .catch(error => {
      console.error('Błąd odczytu danych z bazy danych:', error);
      res.render('notes/index', { 
        locals,
        notes: [],
        layout: "../views/layouts/notes"
      });
    });
};

/**
 * GET /
 * View Specific Note
 */
exports.notespageViewNote = async (req, res) => {
  const note = await Note.findById({ _id: req.params.id });

  if (note) {
    res.render("notes/view", {
      noteID: req.params.id,
      note,
      layout: "../views/layouts/notes",
    });
  } else {
    res.send("Something went wrong.");
  }
};

/**
 * PUT /
 * Update Specific Note
 */
exports.notespageUpdateNote = async (req, res) => {
  try {
    await Note.findOneAndUpdate(
      { _id: req.params.id },
      { 
        title: req.body.title,
        body: req.body.body 
      }
    );
    const accessToken = jwt.sign()
    res.json({ accessToken: accessToken });

    res.redirect("/notes");
  } catch (error) {
    console.log(error);
  }
};

/**
 * DELETE /
 * Delete Note
 */
exports.notespageDeleteNote = async (req, res) => {
  try {
    await Note.deleteOne({ _id: req.params.id });
    res.redirect("/notes");
  } catch (error) {
    console.log(error);
  }
};

/**
 * GET /
 * Add Notes
 */
exports.notespageAddNote = async (req, res) => {
  res.render("notes/add", {
    layout: "../views/layouts/notes",
  });
};

/**
 * POST /
 * Add Notes
 */
exports.notespageAddNoteSubmit = async (req, res) => {
  try {
    await Note.create(req.body);
    res.redirect("/notes");
  } catch (error) {
    console.log(error);
  }
};
