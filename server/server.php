<?php

// Database setup ////////////////////////////////////
define('CONNECTION_STRING', 'mongodb://localhost');
define('DATABASE', 'pacman_experiments');

$connection = new Mongo(CONNECTION_STRING);
$db = $connection->{DATABASE};

// Slim routes etc. //////////////////////////////////
require 'Slim/Slim.php';
\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

$response = $app->response();
$response['Content-Type'] = 'application/json';

/**
 * Get an active experiment if there is one.
 */
$app->get('/active_experiment', function () use ($db, $response) {
    //find an experiment with a count greater than zero, and
    //decrement that count
    $experiment = $db->experiments->findAndModify(
        array('count' => array('$gt' => 0)),
        array('$inc' => array('count' => -1)),
        array('_id' => 1, 'script' => 1)
    );
    
    if ($experiment == null)
        $response->status(410); //HTTP 410 Gone
    else
        jsonEncodeEntity($experiment); 
});

/**
 * Saves a score to the specified experiment.
 */
$app->post('/experiments/:id', function ($id) use ($db, $response) {
    $score = intval(@file_get_contents('php://input'));
    
    $experiment = $db->experiments->findAndModify(
        array('_id' => new MongoId($id)),
        array('$push' => array('scores' => $score)));
    
    if ($experiment == null)
        $response->status(404);
    else
        echo "OK";
});


/**
 * Saves an experiment to the collection.
 */
$app->post('/experiments', function () use ($db) {
    $experiment = json_decode(@file_get_contents('php://input'));
    $db->experiments->save($experiment);
});


$app->run();


// Helper functions //////////////////////////////////

function convertObjectId($entity)
{
    $entity['_id'] = $entity['_id']->{'$id'};
    return $entity;
}

function jsonEncodeEntity($entity)
{
    echo json_encode(convertObjectId($entity));
}
