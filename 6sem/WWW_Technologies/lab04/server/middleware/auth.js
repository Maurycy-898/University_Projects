// middleware/auth.js
const jwt = require('jsonwebtoken');


const authMiddleware = (req, res, next) => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    return res.status(401).json({ message: `Access denied, token: ${token}` });
  }

  jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, note) => {
    if (err) res.status(403).json({ message: `Error verifing, token: ${token}` });
    req.note = note;
    next();
  });
}
module.exports = authMiddleware;
