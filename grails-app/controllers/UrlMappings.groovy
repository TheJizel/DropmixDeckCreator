class UrlMappings {
    static excludes = ["/images/*", "/javascripts/*", "/stylesheets/*"]

    static mappings = {
        "/advanced" controller: "deckCreation", action: "advanced"
        "/" controller: "deckCreation", action: "index"

        "/$controller/$action?/$id?(.$format)?" { }

        "500" view: "/error"
        "/*" redirect: "/"
    }
}

