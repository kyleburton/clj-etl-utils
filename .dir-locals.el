;;; Directory Local Variables
;;; For more information see (info "(emacs) Directory Variables")

((clojure-mode
  (krb-clj-cider-connect-args :host "localhost" :port "4021")
  (krb-clj-cider-connect-fn . cider-connect)
  (ffip-project-root       . "~/code/github.com/kyleburton/clj-etl-utils")

  (krb-ag-search-directory . "~/code/github.com/kyleburton/clj-etl-utils")
  (eval . (progn
            (require 'find-file-in-project)
            (add-to-list 'ffip-prune-patterns "*.clj-kondo")))))
