# My Game List

Application android créée avec Jetpack Compose lors d'un module d'ouverture de ma formation.

## Fonctionnalités
- **Recherche par filtres** des jeux afin de trouver plus rapidement les jeux voulus.
- **Affichage détaillé** des jeux avec des informations comme le titre, la couverture, les genres, les plateformes, etc.
- **Navigation fluide** entre les détails des jeux avec un système de swipe.
- **Gestion des favoris** pour enregistrer et retrouver facilement ses jeux préférés.

## Technologies utilisées
- **Langage :** Kotlin
- **Gestion des favoris :** FavoriteManager (stockage local)

## Pour les profs

### API non fonctionnelle
La récupération du token se fait bien mais l'affichage ne fonctionnait pas avec celle-ci

### BONUS
- Le swipe entre les jeux fonctionne et bloque le premier swipe vers la gauche et le dernier swipe vers la droite. Il fonctionne également lorsque la liste est filtrée
- L'application s'adapte au thème sombre et au thème clair du téléphone
- L'architecture est tiée pour plus de lisibilité
- Les favoris sont sauvegardés dans une base de données et seront conservés même si l'application est fermée
