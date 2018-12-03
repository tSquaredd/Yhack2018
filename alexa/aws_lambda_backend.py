# -*- coding: utf-8 -*-

from ask_sdk_core.skill_builder import SkillBuilder
from ask_sdk_core.dispatch_components import AbstractRequestHandler
from ask_sdk_core.dispatch_components import AbstractExceptionHandler
from ask_sdk_core.utils import is_request_type, is_intent_name
from ask_sdk_core.handler_input import HandlerInput

from ask_sdk_model.ui import SimpleCard
from ask_sdk_model import Response
import requests

sb = SkillBuilder()

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)


class LaunchRequestHandler(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_request_type("LaunchRequest")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response
        speech_text = "Welcome to the waTli Skill, get information on your rooms, devices and more!"

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            False)
        return handler_input.response_builder.response


class HelloWorldIntentHandler(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("HelloWorldIntent")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        r = requests.get('https://api.co2signal.com/v1/latest?countryCode=FR',
                         headers={"auth-token": "1f86c8aedef8e999"})
        # str(r.json()["countryCode"])

        # speech_text = "YOOO"

        speech_text = "hey this is it: " + str(r.json()["countryCode"])

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class getDevicesIntent(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("getDevicesIntent")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        # r = requests.get('https://api.co2signal.com/v1/latest?countryCode=FR', headers={"auth-token":"1f86c8aedef8e999"})

        speech_text = "Here are the outlets available: "

        r = requests.get('https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceNames')

        for val in r.json():
            speech_text = speech_text + ", " + str(r.json()[str(val)])


        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class getSpecificDevice(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("getSpecificDevice")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        slots = handler_input.request_envelope.request.intent.slots

        obj = slots["device"]

        print("obj: " + str(obj.value))

        r = requests.get('https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceNames')

        fossil_fuel_percentage = '43.83'
        carbon_tax = ''

        devices = []

        for val in r.json():
            devices.append(str(r.json()[str(val)]).lower())

        if obj.value.lower() not in devices:
            speech_text = "sorry, I don't see any information on that device"
        else:
            print("rjson: " + str(r.json()))
            for val in r.json():
                if r.json()[val].lower() == obj.value:
                    r_1 = requests.get('https://us-central1-yhack2018-77c5f.cloudfunctions.net/getLatest?device=' + val)
                    rt = requests.get('https://us-central1-yhack2018-77c5f.cloudfunctions.net/getCarbonAnalysis?device=' + val)

                    carbon_tax = rt.json()["carbon"]["total"]

                    speech_text = str(obj.value) + "'s wattage is " + str(r_1.json()["watts"]) + " watts. " + "It has a fossil fuel percentage of " + str(fossil_fuel_percentage) + \
                                  " percent and a total carbon tax of " + str(carbon_tax) + " grams of co2 equivalent per kilowatt hour"

                    handler_input.response_builder.speak(speech_text).set_card(
                        SimpleCard("Hello World", speech_text)).set_should_end_session(
                        True)
                    return handler_input.response_builder.response
            speech_text = "error"

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class listIntents(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("listIntents")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        speech_text = "There's quite a lot I can do! I can tell you all of your outlets, I can create timers for you, lock devices and even give you stats about power usage and " \
                      "where your outlet's energy comes from! "

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class setLockForDevice(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("setLockForDevice")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        slots = handler_input.request_envelope.request.intent.slots

        obj = slots["device"]

        r = requests.get('https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceNames')

        devices = []

        for val in r.json():
            devices.append(str(r.json()[str(val)]).lower())

        if obj.value.lower() not in devices:
            speech_text = "sorry, I don't see any information on that device"
        else:
            speech_text = "setting lock on " + str(obj.value)

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class setTimerForDevice(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("setTimerForDevice")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        slots = handler_input.request_envelope.request.intent.slots

        obj_device = slots["device"].value
        obj_amount = slots["amount"].value
        obj_time = slots["time"].value

        # if obj_device dne in firebase return request then return speech text
        # else: do other things

        if obj_time == 'hour' or obj_time == 'hours' and obj_amount > 24:
            speech_text = "that seems too long for a timer, check back tomorrow to set that up"

        speech_text = "setting lockout time on " + str(obj_device) + " for " + str(obj_amount) + " " + str(obj_time)

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class turnOnDevice(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("turnOnDevice")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        slots = handler_input.request_envelope.request.intent.slots

        obj = slots["device"]

        device_name = str(obj.value).lower()
        device_id = ''

        rj = requests.get("https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceNames")

        for val in rj.json():
            if rj.json()[val].lower() == device_name:
                device_id = str(val)

        speech_text = "I turned on the " + device_id

        # device is the key that has value of device_name
        r = requests.post("https://us-central1-yhack2018-77c5f.cloudfunctions.net/setStatus?time=000&device=" + device_id + "&isOn=true")

        if r.text == "Worked":
            speech_text = "Sucessfully turned on " + device_name
        else:
            speech_text = "I was unable to turn on " + device_name

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class turnOffDevice(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("turnOffDevice")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        slots = handler_input.request_envelope.request.intent.slots

        obj = slots["device"]

        device_name = str(obj.value).lower()
        device_id = ''

        rj = requests.get("https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceNames")

        for val in rj.json():
            if rj.json()[val].lower() == device_name:
                device_id = str(val)

        speech_text = "I turned off the " + device_id

        # device is the key that has value of device_name
        r = requests.post("https://us-central1-yhack2018-77c5f.cloudfunctions.net/setStatus?time=000&device=" + device_id + "&isOn=false")

        if r.text == "Worked":
            speech_text = "Sucessfully turned off " + device_name
        else:
            speech_text = "I was unable to turn off " + device_name

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class getUsage(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("getUsage")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        slots = handler_input.request_envelope.request.intent.slots

        obj = slots["amt"]

        print("obj name: " + str(obj.value))

        usage_value = ''

        if obj.value == "total":
            r = requests.get("https://us-central1-yhack2018-77c5f.cloudfunctions.net/getHomeUsage")
            usage_value = r.json()["watts"]
        # elif obj.name == "current":
        else:
            r = requests.get("https://us-central1-yhack2018-77c5f.cloudfunctions.net/getCurrentHomeUsage")
            usage_value = r.json()["watts"]

        speech_text = "The " + str(obj.value) + " home usage is " + str(usage_value) + " watts"

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


class getDeviceUsage(AbstractRequestHandler):

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("getDeviceUsage")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response

        slots = handler_input.request_envelope.request.intent.slots

        obj_device = slots["device"]
        obj_amt = slots["amt"]

        #===================

        r = requests.get('https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceNames')

        devices = []

        for val in r.json():
            devices.append(str(r.json()[str(val)]).lower())

        # ===================

        if obj_device.value in devices:
            print("device is in devices")
            print("amt value: " + str(obj_amt.value))
            if obj_amt.value == "total":
                print("obj device: " + str(obj_device.value))

                for val in r.json():
                    if r.json()[val].lower() == obj_device.value:
                        rj = requests.get("https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceTotalUsage?device=" + str(val))
                        print("request json: " + str(rj.json()))
                        usage_value = rj.json()["watts"]
            else:
                for val in r.json():
                    if r.json()[val].lower() == obj_device.value:
                        rj = requests.get("https://us-central1-yhack2018-77c5f.cloudfunctions.net/getLatest?device=" + str(val))
                        usage_value = rj.json()["watts"]
        else:
            print("unable to find device")
            speech_text = "unable to find device"
            handler_input.response_builder.speak(speech_text).set_card(
                SimpleCard("Hello World", speech_text)).set_should_end_session(
                True)
            return handler_input.response_builder.response

        speech_text = "The " + str(obj_amt.value) + " usage for " + str(obj_device.value) + " is " + str(usage_value) + " watts"

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text)).set_should_end_session(
            True)
        return handler_input.response_builder.response


# -------------DEFAULT HANDLERS------------

class HelpIntentHandler(AbstractRequestHandler):
    """Handler for Help Intent."""

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("AMAZON.HelpIntent")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response
        speech_text = "You can say hello to me!"

        handler_input.response_builder.speak(speech_text).ask(
            speech_text).set_card(SimpleCard(
            "Hello World", speech_text))
        return handler_input.response_builder.response


class CancelOrStopIntentHandler(AbstractRequestHandler):
    """Single handler for Cancel and Stop Intent."""

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return (is_intent_name("AMAZON.CancelIntent")(handler_input) or
                is_intent_name("AMAZON.StopIntent")(handler_input))

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response
        speech_text = "Goodbye!"

        handler_input.response_builder.speak(speech_text).set_card(
            SimpleCard("Hello World", speech_text))
        return handler_input.response_builder.response


class FallbackIntentHandler(AbstractRequestHandler):
    """AMAZON.FallbackIntent is only available in en-US locale.
    This handler will not be triggered except in that locale,
    so it is safe to deploy on any locale.
    """

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_intent_name("AMAZON.FallbackIntent")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response
        speech_text = (
            "The Wattli skill can't help you with that.  "
            "You can ask what I do!!")
        reprompt = "You can ask what I do!!"
        handler_input.response_builder.speak(speech_text).ask(reprompt)
        return handler_input.response_builder.response


class SessionEndedRequestHandler(AbstractRequestHandler):
    """Handler for Session End."""

    def can_handle(self, handler_input):
        # type: (HandlerInput) -> bool
        return is_request_type("SessionEndedRequest")(handler_input)

    def handle(self, handler_input):
        # type: (HandlerInput) -> Response
        return handler_input.response_builder.response


class CatchAllExceptionHandler(AbstractExceptionHandler):
    """Catch all exception handler, log exception and
    respond with custom message.
    """

    def can_handle(self, handler_input, exception):
        # type: (HandlerInput, Exception) -> bool
        return True

    def handle(self, handler_input, exception):
        # type: (HandlerInput, Exception) -> Response
        logger.error(exception, exc_info=True)

        speech = "Sorry, there was some problem. Please try again!!"
        handler_input.response_builder.speak(speech).ask(speech)

        return handler_input.response_builder.response


sb.add_request_handler(LaunchRequestHandler())
sb.add_request_handler(HelloWorldIntentHandler())

#
sb.add_request_handler(getDevicesIntent())
sb.add_request_handler(getSpecificDevice())
sb.add_request_handler(setTimerForDevice())
sb.add_request_handler(listIntents())
sb.add_request_handler(setLockForDevice())
sb.add_request_handler(turnOnDevice())
sb.add_request_handler(turnOffDevice())
sb.add_request_handler(getUsage())
sb.add_request_handler(getDeviceUsage())
#

sb.add_request_handler(HelpIntentHandler())
sb.add_request_handler(CancelOrStopIntentHandler())
sb.add_request_handler(FallbackIntentHandler())
sb.add_request_handler(SessionEndedRequestHandler())

sb.add_exception_handler(CatchAllExceptionHandler())

handler = sb.lambda_handler()
