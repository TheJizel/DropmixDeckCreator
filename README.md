# Dropmix Deck Creator

The <a href="https://github.com/TheJizel/DropmixDeckCreator">Dropmix Deck Creator</a> is a web application that creates
semi-random sets of cards suitable for various modes of play in <a href="https://www.dropmix.com/">Dropmix</a>.

The logic uses templates (based on the original playlists from the retail sets) and a pool of available cards (as 
specified by the user) to curate an evenly distributed mix with respect to the playlist that each card belongs to (i.e., 
using multiple cards from the same playlist is reduced as much as possible given the diversity of available cards).

For each deck, the user will get the following:
 - One level 2 wildcard
 - One red card for each level (1, 2, and 3)
 - One blue card for each level (1, 2, and 3)
 - One level 2 card and one level 3 for both yellow and green
 
From here, they'll have a chance of getting one of the following:
 - One level 1 yellow card and one level 1 green card
 
    or
 
 - An additional level 1 red card and an additional level 1 blue card
 
Finally, the two FX cards they get can depend on certain scenarios:
 - Two level 2 (in either scenario)
 - A level 2 and a level 3 (in either scenario)
 - A level 1 and a level 3 (in the extra red and blue card scenario)
 - A level 1 and a level 2 (in the extra yellow and green card scenario)
 - Two level 3 (in the extra red and blue card scenario)
 
Note that at least 15 cards are required for creating a deck and the composition of those cards must match at least 1 of
the templates as specified above. If this is not the case, the page will simply display an error.
  
## Setup

### Prerequisites

- <a href="https://sdkman.io/" target="_blank">SDKMAN!</a>
    - Used to configure Grails 4
- <a href="https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html">Java Runtime Environment (JRE) 8</a>

### Downloading / Configuring Grails 4

1. Configure SDKMAN by following the steps on the "<a href="https://sdkman.io/install">Install</a>" page.
2. After installing SDKMAN, run `sdk install grails 4.0.0` to install Grails 4.
3. Run `sdk list grails` to ensure that the appropriate version of Grails is in use. If not, 
run `sdk use grails 4.0.0` to switch.

### Running the project 

1. Download the project to your local machine (using <a href="https://git-scm.com/">Git</a> or by retrieving then
extracting the <a href="https://github.com/TheJizel/DropmixDeckCreator/archive/master.zip">zip</a> containing the 
project files).
2. Run `grails run-app` from the project root folder.
3. Navigate to http://localhost:8080 in your browser once the application has started.

## Unit tests

The project includes unit tests covering all the functionality of the back-end classes. You can execute these by running
`grails test-app` from the project's root directory.

Additionally, the project includes `.gitlab-ci.yml` and `.github/workflows/github-ci.yml` for configuration of GitLab CI
and CI via GitHub Actions respectively. No additional setup is required to utilize these features and the unit tests 
will execute automatically on those platforms whenever changes are made.
