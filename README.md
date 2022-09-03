# rabbitmq-practice

I. Implement a Spring Boot application for sending notifications to customers
about the receipt of goods based on the following architecture:

<img width="813" alt="Screen Shot 2022-09-04 at 03 57 43" src="https://user-images.githubusercontent.com/52571030/188290010-6e30f9c3-0289-4adb-9117-ca010f6c9543.png">

Notes:
1. Failed Message Exchange is not configured as DLX for the source queues.
Consumer is responsible to re-publish failed messages.
II. Update previous implementation and change retry mechanism from inprocess to retry exchange/queue. Retry queue should have ttl, after message
expires it should be routed to the source queue.

Notes:
1. Retry exchange is not configured as DLX for the source queues. Consumer
is responsible to re-publish messages for retry. If retry limit reached
message should be re-published to Failed Message Exchange instead.
III. Update previous implementation, configure message ttl, max size and dlx
attributes on consumer queues. Expired messages or during queue overflow
messages should be sent to DLQ by broker.

Notes:
1. Dead Letter exchange should be specified as DLX attribute on source
queues in addition to message TTL and max length attributes.


<img width="936" alt="Screen Shot 2022-09-04 at 03 59 25" src="https://user-images.githubusercontent.com/52571030/188290042-2a7179bc-63b3-4f0d-b3f4-b341ffbd8c4a.png">
