#!/usr/bin/env bash
# Compile la version web de production et la copie dans deploy/web/site,
# prête à être déployée par Railway (Root Directory = deploy/web).
set -euo pipefail
cd "$(dirname "$0")/.."

./gradlew :composeApp:wasmJsBrowserDistribution

OUT=composeApp/build/dist/wasmJs/productionExecutable
rm -rf deploy/web/site
mkdir -p deploy/web/site
cp -R "$OUT"/. deploy/web/site/

rm -f deploy/web/site/*.map
echo "OK : site prêt dans deploy/web/site ($(du -sh deploy/web/site | cut -f1))"
echo "Pour déployer : commit + push de deploy/web/site, Railway redéploie automatiquement"
