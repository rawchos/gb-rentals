# GB Rentals

A basic, no frills, functional UI to rent games using the [GiantBomb](https://www.giantbomb.com/) API. 

## Running the App

### Back End

First, get an api key from the [GiantBomb API](https://www.giantbomb.com/api/) and create a config file with this key included at `resources/config.edn`:

```edn
{:api-key "your_key_here"}
```

Start the backend api using [leiningen](https://leiningen.org/). This will also download any dependencies needed. When the api starts, it will be listening for requests on [http://localhost:3000](http://localhost:3000).

```sh
lein run
```

### Front End

Start a temporary local web server, build the app with the `dev` profile, and serve the app,
browser test runner and karma test runner with hot reload:

```sh
npm install
npx shadow-cljs watch app
```

Please be patient; it may take over 20 seconds to see any output, and over 40 seconds to complete.

When `[:app] Build completed` appears in the output, browse to
[http://localhost:8280/](http://localhost:8280/).
