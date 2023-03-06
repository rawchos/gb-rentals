(ns gb-rentals.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [gb-rentals.subs :as subs]))

(defn game-preview
  [{:keys [name deck image guid]}]
  (let [rentals @(rf/subscribe [::subs/games-to-rent])
        image-url (:superUrl image)
        in-cart? (fn [guid]
                  (-> (keys rentals)
                      (set)
                      (contains? guid)))]
    [:div.game-container 
     [:div.game-header 
      [:img {:src image-url :alt "Game Image"}]
      [:button.btn.btn-primary.btn-sm.pull-xs-right {:on-click #(rf/dispatch [:toggle-cart guid])}
       (if (in-cart? guid) "Remove from Cart" "Add to Cart")]]
     [:div.game-description 
      [:h1 name]
      [:p deck]]]))

(defn games-list [games]
  [:div
   (for [game games]
     ^{:key (:guid game)} [game-preview game])])

(defn search-results []
  (let [results @(rf/subscribe [::subs/search-results])]
    [:div.search-results
     [:div.container
      [:div.row
       [:div.col-xs-12.col-md-10.offset-md-1
        [games-list results]]]]]))

(defn cart []
  (let [games-to-rent @(rf/subscribe [::subs/games-to-rent])]
    [:div.current-rentals
     [:div.container
      [:div.row
       [:div.col-xs-12.col-md-10.offset-md-1
        [games-list (flatten (vals games-to-rent))]]]]]))

(defn search-form []
  (let [current-search @(rf/subscribe [::subs/search-text])
        text-input (reagent/atom {:search-text current-search})]
    (fn []
      (let [{:keys [search-text]} @text-input
            search-games (fn [event search-text]
                           (.preventDefault event)
                           (rf/dispatch [:search-games search-text]))]
        [:div.search-form
        [:div.container
         [:div.row
          [:div.col-md-6.offset-md-3.col-xs-12
           [:form {:on-submit #(search-games % (:search-text @text-input))}
            [:fieldset.form-group
             [:input.form-control.form-control-lg {:type        "text"
                                                   :placeholder "Games"
                                                   :value       search-text
                                                   :on-change   #(swap! text-input assoc :search-text (-> % .-target .-value))
                                                   :disabled    false}]]
            [:button.btn.btn-lg.btn-primary.pull-xs-right "Search"]]]]]]))))

(defn header []
  (let [active-page @(rf/subscribe [::subs/active-page])
        games-to-rent @(rf/subscribe [::subs/games-to-rent])]
    [:nav.navbar.navbar-light
     [:div.container
      [:ul.nav.navbar-nav.pull-xs-right
       [:li.nav-item
        [:a.nav-link {:on-click #(rf/dispatch [:set-active-page :search])
                      :class (when (= active-page :search) "active")} "Search"]]
       (when (seq games-to-rent)
         [:li.nav-item
          [:a.nav-link {:on-click #(rf/dispatch [:set-active-page :cart])
                        :class (when (= active-page :cart) "active")} "Cart"]])]]]))

(defn search []
  [:div.container.page
   [search-form]
   [search-results]])

(defn pages [page-name]
  (case page-name
    :search [search]
    :cart [cart]
    [search]))

(defn main-panel []
  (let [active-page @(rf/subscribe [::subs/active-page])]
    [:div
     [header]
     [pages active-page]]))
