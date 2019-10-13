# PLEASE NOTE, THIS PROJECT IS NO LONGER BEING MAINTAINED
Visit the new developer portal for MoMo the updated api [here](https://momodeveloper.mtn.com)
# MTN MOBILE MONEY SDK
This is a sample project to show how to use the mobile money sdk to accept payments in your android app via mobile money.

# PREVIEW
----
| Enter phone number  | Wait for bill prompt | Confirm transaction | Success | Error |
| ------------- | ------------- | ------------- | ------------- | ------------- |
| ![Screenshot 1](https://github.com/norrisboat/MobileMoneySDK/blob/master/Screenshots/sho1.png?raw=true "Enter phone number")  | ![Bill prompt](https://github.com/norrisboat/MobileMoneySDK/blob/master/Screenshots/shot2.png?raw=true "Wait for bill prompt")  | ![confirm](https://github.com/norrisboat/MobileMoneySDK/blob/master/Screenshots/shot3.png?raw=true "Confirm transaction") | ![success](https://github.com/norrisboat/MobileMoneySDK/blob/master/Screenshots/shot4.png?raw=true "Transaction successful") | ![Error occured](https://github.com/norrisboat/MobileMoneySDK/blob/master/Screenshots/shot5.png?raw=true "Error occured")|

# How to install
---------------------------------------
To use the sdk in your project:

1. Download the project zip file.
2. Unzip the file.
3. In android studio right file -> new - import module and go to the path for the zip file -> open the folder -> and click on paywithmobilemoney.
4. Change the name of your module if you wish and click next.
5. Wait till the build finishes and you now have the paywithmobilemoney sdk.
    

From Android Studio :
```
->Right click your project folder 
        ->Open Module Settings 
                ->Select the Dependency Tab 
                          ->Tap on the green plus sign on the right and select "Module dependency" 
                                    ->You would see the "paywithmobilemoney" module. Select it.
```

Tap on OK and you are good to add it in your build.gradle file.

# Usage
---------------------------------------
1. Import necessary dependencies
And add this to your project build.gradle file 
```gradle

repositories {
    maven { url 'https://oss.sonatype.org/content/repositories/ksoap2-android-releases/' }
}

dependencies {
    compile 'com.google.code.ksoap2-android:ksoap2-android:3.6.0'
}

```
In your application build.gradle add this line
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```
2. Initialize sdk preferably in your Application class to avoid initializing it multiple time. 
[How to create an Application class](https://github.com/codepath/android_guides/wiki/Understanding-the-Android-Application-Class)
```java
//all parameters are Strings
//username and password refers to the credentials associated with your merchant account from MTN
  new MobileMoney(YourActivity.this).initCredentials(username,password);
```

3. Performing transactions.
```java
//Performing transaction with provided ui
MobileMoney mobileMoney = new MobileMoney(YourActivity.this);
mobileMoney.makePurchaseWithDialog(YourActivity.this, "itemName", "description", billprompt , amount, "phoneNumber", "message", new OnTransactionInitiated() {
                @Override
                public void onInvoiceMade(String invoiceNumber) {
                    //returns invoice number before status is checked
                }

                @Override
                public void OnTransactionStarted() {
                    //transaction has started
                }

                @Override
                public void OnTransactionSuccessful(String invoiceNumber) {
                    //user has paid for the item and returns an invoice number
                }

                @Override
                public void OnTransactionFailed(String errorCode) {
                    //transaction failed because of errorCode
                }
            });
            }
        });
        
//Call this method instead if you want to implement your own ui for the transaction
mobileMoney.makePurchase(YourActivity.this, "itemName", "description", billprompt , amount, "phoneNumber", "message", new OnTransactionInitiated() {
                @Override
                public void onInvoiceMade(String invoiceNumber) {
                    //returns invoice number before status is checked
                }

                @Override
                public void OnTransactionStarted() {
                    //transaction has started
                }

                @Override
                public void OnTransactionSuccessful(String invoiceNumber) {
                    //user has paid for the item and returns an invoice number
                }

                @Override
                public void OnTransactionFailed(String errorCode) {
                    //transaction failed because of errorCode
                }
            });
            }
        });
```
**You can now recieve payment through your app with Mobile Money**

# NOTE
Don't call both mobileMoney.makePurchase and mobileMoney.makePurchaseWithDialog at the same time. **makePurchaseWithDialog** is used when you want payment flow to use the provided user interface. **makePurchase** is used when you want to provide your own interface using the callbacks.

# ADDITIONAL METHODS
-----
1. **GET ALL INVOICES**
This method is used to retrieve all of the users invoices along with their statuses. Useful when manual verification of invoice numbers is needed to cater for time out errors. 
**NB: all this data will be lost if the user clears the app data. Server solutions should be put in place by the developer to avoid this.**

```java
    MobileMoney mobileMoney = new MobileMoney(this);
    mobileMoney.getInvoices(new OnInvoicesRequested() {
            @Override
            public void OnRequestStarted() {

            }

            @Override
            public void OnTransactionSuccessful(ArrayList<Invoice> invoices) {
                //returns an array list of invoices
            }

            @Override
            public void OnNoInvoicesFound() {

            }
        });
```

The Invoice class consists of:
* _name_ - corresponds to item name
* _invoiceNumber_ - invoice number generated by MTN
* _isValid_ - is true when invoice number is correct
* _isStatusChecked_ - is true when the status of this invoice has been checked to verify if bill was paid or not. It will be false if status was checked but because of timeout issues it wasn't verified. Developers should check for the status of the invoice manually when it's false.

2. **CHECK INVOICE STATUS**
This method is used to manually check the status of an invoice number.
Because of timeout issues, the system might be down for sometime. All transactions are saved by the sdk and can manually be checked for its status at anytime. It is also useful when the user after paying didn't confirm the transaction.

```java
MobileMoney mobileMoney = new MobileMoney(this);
mobileMoney.checkInvoiceStatus("invoiceNumber", new OnInvoiceStatusRequested() {
            @Override
            public void OnTransactionStarted() {

            }

            @Override
            public void OnTransactionSuccessful() {

            }

            @Override
            public void OnTransactionFailed(String errorCode) {

            }
        });
```
3. **SET THIRD PARYT ID**
This optional method is set a third party id if applicable.A default one is provided by the sdk. When applicable should go in your application class.
```java
new MobileMoney(YourActivity.this).setThirdPartyId("youtThirdPartyId");
```

# ADDITIONAL INFO
----
* _Amount_ - the amount the user is supposed to pay. **All transactions made with the module are live and therefore for testing purposes use small amounts like 0.01.**
* _Billprompt_ - Values are 0,2 and 3. If set to 2 asynchronous billprompt will be initiated. If set to 3, an sms containing the invoiceNo is sent automatically to the reciepient should the bill payment fail due to time out or insufficient funds or when an invalid mobile money number is supplied. **3 is normally used.**
* _ThirdPartyId_ - This is a required field only when billprompt is used. The field is provided by the merchant during a transaction and must be unique. Example: ADG344566786
*  _Error codes:_ - various error codes and their meaning.
    * _"21VD"_ - Bill wasn't paid  
    * _"NIC"_ - Internet connectivity isn't available
    * _"NIN"_ - No Invoice Number
    * _"DC"_ - Dialog closed
