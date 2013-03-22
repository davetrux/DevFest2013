    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
--------------------------
<service android:name=".RestService"></service>    
--------------------------
public static final String ACTION="REST-PERSONS";

---------------------------

        int result = Activity.RESULT_CANCELED;

        //Get the url from the intent
        Uri data = intent.getData();
        String url = data.getScheme() + "://" + data.getHost() + data.getPath();

        //Fetch the data
        String people = getJson(url);

        if(!people.equalsIgnoreCase("")) {
            result = Activity.RESULT_OK;
        }

        //Send back the results
        sendResult(result, people);
-------------------------------

    /*
     * Get the JSON results from the web service
     */
    private String getJson(String url) {
        WebHelper http = new WebHelper();
           String webResult;
           try {
               webResult = http.getHttp(url);

           } catch (IOException e) {
               webResult = "";
               Log.d(getClass().getName(), "Exception calling service", e);
           }

           return webResult;
    }
    
-------------------------- 

    /*
     * Place the results into an intent and return it to the caller
     */
    private void sendResult(int result, String personJson) {

        Intent sendBack = new Intent(ACTION);

        sendBack.putExtra("result", result);
        sendBack.putExtra("personlist", personJson);

        //Keep the intent local to the application
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendBack);
    }

---------------------------------

   
                    //Check to see if we can connect
                if(WebHelper.isOnline(getApplicationContext())) {
                    //Send an intent to the service to get data
                    Intent intent = new Intent(Main.this, RestService.class);
                    intent.setData(Uri.parse("http://devfestdetroit.appspot.com/api/names/10"));
                    startService(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not currently online", Toast.LENGTH_SHORT).show();
                }

----------------------------
    /*
     * Hookup the BroadcastManager to listen to service returns
     */
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(RestService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
    }
    
    /*
     * Helper method to put the list of persons into the ListView
     */
    private void BindPersonList() {
        PersonAdapter adapter = new PersonAdapter(Main.this, mData);
        mPersonList.setAdapter(adapter);
    }

    /*
     * The listener that responds to intents sent back from the service
     */
    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int serviceResult = intent.getIntExtra("result", -1);
            if (serviceResult == RESULT_OK) {
                String json = intent.getStringExtra("personlist");
                Gson parser = new Gson();
                mData = parser.fromJson(json, new TypeToken<ArrayList<Person>>(){}.getType());

                BindPersonList();

            } else {
                Toast.makeText(Main.this, "Rest call failed.", Toast.LENGTH_LONG).show();
            }

            Log.d("BroadcastReciever", "onReceive called");
        }
    };
------------------------------

		/*
         * Constructor needed for parcelable object creation
         */
        public Person(Parcel item) {
            mFirstName = item.readString();
            mLastName = item.readString();
            mGender = item.readString();
        }
        
    /*
     * Used to generate parcelable classes from a parcel
     */
    public static final Parcelable.Creator<Person> CREATOR
            = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    /*
     * Needed to implement parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /*
     * Place the data into the parcel
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFirstName);
        parcel.writeString(mLastName);
        parcel.writeString(mGender);
    }
    
----------------------------------------------
    /*
     * Persist the list data during rotations
     */
    @Override
   	public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("PersonData", mData);
    }

    /*
     * Unhook the BroadcastManager that is listening for service returns before rotation
     */
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
    }
    
--------------------------------------

        //Put back the person data if it was persisted due to rotation
        if(savedInstanceState != null && savedInstanceState.containsKey("PersonData")) {
            mData = savedInstanceState.getParcelableArrayList("PersonData");
            BindPersonList();
        }