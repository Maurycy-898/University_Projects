const express= require('express');
const router = express.Router();
const mainController = require('../controllers/mainController');
const authMiddleware = require('../middleware/auth');

/**
 * App Routes:
 */
router.get('/', mainController.homepage);
router.get('/notes', authMiddleware, mainController.notespage);

router.get('/notes/item/:id', authMiddleware, mainController.notespageViewNote);
router.post('/notes/item/:id', mainController.notespageUpdateNote);

router.get('/notes/add', authMiddleware, mainController.notespageAddNote);
router.put('/notes/add', mainController.notespageAddNoteSubmit);
router.delete('/notes/item-delete/:id', mainController.notespageDeleteNote);

module.exports = router;
