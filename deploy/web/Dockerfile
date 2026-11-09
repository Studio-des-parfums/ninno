# Sert la version web (Kotlin/Wasm) de Ninno avec nginx.
# Le dossier site/ est généré par ../../scripts/build-web.sh (compilation faite en local).
FROM nginx:1.27-alpine

# nginx remplace ${PORT} dans ce template au démarrage (Railway fournit PORT)
COPY nginx.conf.template /etc/nginx/templates/default.conf.template
COPY site/ /usr/share/nginx/html/

ENV PORT=8080
EXPOSE 8080
