(ns cljs-eval-example.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [environ.core :refer [env]]))

(def mount-target
  [:div#app])

(def loading-page
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css
      ;; "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.8.0/codemirror.min.css"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/codemirror.min.css"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/addon/dialog/dialog.min.css" ;; to show vim messages
      "//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.9.1/styles/default.min.css"
      ;; themes
      ;; "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/theme/abcdef.min.css"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/theme/monokai.min.css"
      ;; "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/theme/midnight.min.css"
      (if (env :dev) "css/site.css" "css/site.min.css"))]
    [:body
     mount-target
     (include-js
      "//cdnjs.cloudflare.com/ajax/libs/highlight.js/8.9.1/highlight.min.js"
      ;; "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.8.0/codemirror.min.js"
      ;; "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.8.0/mode/clojure/clojure.min.js"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/codemirror.min.js"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/addon/dialog/dialog.min.js" ;; to show vim messages
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/addon/edit/matchbrackets.min.js"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/addon/edit/closebrackets.min.js"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/addon/search/searchcursor.min.js"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/mode/clojure/clojure.min.js"
      "//cdnjs.cloudflare.com/ajax/libs/codemirror/5.20.2/keymap/vim.min.js"
      "js/app.js")]]))


(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)

  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (wrap-defaults #'routes site-defaults)]
    (if (env :dev) (-> handler wrap-exceptions wrap-reload) handler)))
