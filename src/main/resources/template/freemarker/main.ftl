<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/bootstrap.css"> <!-- this isn't added yet -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/c3/0.1.29/c3.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
  <body>
     ${content}
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/bootstrap.js"></script> <!-- not yet added -->
     <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.4.11/d3.js"></script>
     <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/c3/0.1.29/c3.js"></script>
		<script src="js/main.js"></script>
		<script src="js/signin.js"></script>
		<script src="js/signup.js"></script>
		<script src="js/natalie.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
