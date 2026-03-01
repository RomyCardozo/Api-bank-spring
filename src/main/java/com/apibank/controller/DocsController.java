package com.apibank.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocsController {

    @GetMapping(value = {"/api/docs", "/api/docs/"}, produces = MediaType.TEXT_HTML_VALUE)
    public String scalarDocs() {
        return """
                <!doctype html>
                <html lang=\"en\">
                <head>
                  <meta charset=\"utf-8\" />
                  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />
                  <title>API Bank - Scalar Docs</title>
                  <style>
                    body { margin: 0; }
                  </style>
                </head>
                <body>
                  <script id=\"api-reference\" data-title=\"API Bank Spring\"></script>
                  <script>
                    const apiReference = document.getElementById('api-reference');
                    apiReference.setAttribute('data-url', window.location.origin + '/v3/api-docs');
                  </script>
                  <script src=\"https://cdn.jsdelivr.net/npm/@scalar/api-reference\"></script>
                </body>
                </html>
                """;
    }
}
