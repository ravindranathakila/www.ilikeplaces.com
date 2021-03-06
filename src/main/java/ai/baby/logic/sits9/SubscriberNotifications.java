package ai.baby.logic.sits9;

import ai.baby.logic.modules.Modules;
import ai.baby.util.*;
import ai.hbase.Cell;
import ai.hbase.HBaseCrudService;
import ai.hbase.Row;
import ai.hbase.RowResponse;
import ai.ilikeplaces.entities.GeohashSubscriber;
import ai.baby.logic.mail.SendMailLocal;
import ai.baby.rbs.RBGet;
import ai.baby.servlets.Unsubscribe;
import ai.scribble.License;
import com.google.gson.Gson;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Apr 29, 2010
 * Time: 4:10:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class SubscriberNotifications extends AbstractSLBCallbacks implements SubscriberNotificationsRemote {

    public static final int TGIF_TIMING = Integer.parseInt(RBGet.getGlobalConfigKey("TGIF_TIMING"));

    public static final int TGIF_ON_STARTUP = Integer.parseInt(RBGet.getGlobalConfigKey("TGIF_ON_STARTUP"));

    private static final String SUBSCRIBER_EMAIL = "ai/ilikeplaces/logic/Listeners/widgets/subscribe/SubscriberEventEmail.html";

    private static final String SUBSCRIBER_EMAIL_EVENT = "ai/ilikeplaces/logic/Listeners/widgets/subscribe/SubscriberEventEmailEvent.html";

    private static final String SUBSCRIBER_EMAIL_PLACE = "ai/ilikeplaces/logic/Listeners/widgets/subscribe/SubscriberEventEmailPlace.html";

    private static final String EMAIL_FRAME = "ai/ilikeplaces/EmailFrame.xhtml";

    private static final String NAME = "name";

    private static final String LATITUDE = "latitude";

    private static final String LONGITUDE = "longitude";

    @Resource
    private TimerService timerService;

    @EJB
    private SendMailLocal sendMailLocal;

    @Resource(name = "ilpUn")
    private DataSource dataSource;

    @Override
    public void startTimer() {
        final int timeout = TGIF_TIMING == 0
                ? 7 //Days of Week
                * 24 //Hour of Day
                * 60 //Minutes of Hour
                * 60 //Seconds of Minute
                * 1000 //Milliseconds of Second
                : TGIF_TIMING;

        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.INFO, Loggers.CODE_MEMC +
                "HELLO, STARTING " + this.getClass().getSimpleName() + " TIMER  WITH TIMER INTERVAL:" + timeout,
                60000,
                null,
                true);

        final Calendar _calendar = Calendar.getInstance();
        final int todayDay = _calendar.get(Calendar.DAY_OF_WEEK);
        if (todayDay != Calendar.FRIDAY) {
            _calendar.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - todayDay + 6) % 7);
        }

        if (TGIF_TIMING == 0) {
            final Date _time = _calendar.getTime();
            timerService.createTimer(_time, timeout, null);
            String format = new SimpleDateFormat("yyyy/MM/dd").format(_time);
            sl.appendToLogMSG("Starting timer on " + format);
        } else {
            timerService.createTimer(0, timeout, null);
        }

        if (TGIF_ON_STARTUP == 1) {
            final Calendar now = Calendar.getInstance();
            now.add(Calendar.SECOND, 30);
            timerService.createTimer(now.getTime(), timeout, null);
        }

        sl.complete(Loggers.LEVEL.INFO, Loggers.DONE);
    }

    @Timeout
    synchronized public void timeout(final Timer timer) throws IOException, SAXException, TransformerException, JSONException, SQLException {

        final HBaseCrudService<GeohashSubscriber> _geohashSubscriberHBaseCrudService = new HBaseCrudService<GeohashSubscriber>();
        final HBaseCrudService<GeohashSubscriber>.Scanner _scanner = _geohashSubscriberHBaseCrudService.scan(new GeohashSubscriber(), 1).returnValueBadly();

        while (_scanner.getNewValue() != null) {
            final String _newValue = _scanner.getNewValue();
            Loggers.debug("Scanned value:" + _newValue);
            final RowResponse _rowResponse = new Gson().fromJson(_newValue, RowResponse.class);
            Loggers.debug("Scanned as GSON:" + _rowResponse.toString());

            for (final Row _row : _rowResponse.Row) {

                final BASE64Decoder _base64DecoderRowKey = new BASE64Decoder();
                final byte[] _bytes = _base64DecoderRowKey.decodeBuffer(_row.key);
                final String rowKey = new String(_bytes);
                Loggers.debug("Decoded row key:" + rowKey);

                for (final Cell _cell : _row.Cell) {
                    final BASE64Decoder _base64DecoderValue = new BASE64Decoder();
                    final byte[] _valueBytes = _base64DecoderValue.decodeBuffer(_cell.$);
                    final String _cellAsString = new String(_valueBytes);
                    Loggers.debug("Cell as string:" + _cellAsString);
                    final GeohashSubscriber _geohashSubscriber = new GeohashSubscriber();

                    final DatumReader<GeohashSubscriber> _geohashSubscriberSpecificDatumReader = new SpecificDatumReader<GeohashSubscriber>(_geohashSubscriber.getSchema());
                    final BinaryDecoder _binaryDecoder = DecoderFactory.get().binaryDecoder(_valueBytes, null);
                    final GeohashSubscriber _read = _geohashSubscriberSpecificDatumReader.read(_geohashSubscriber, _binaryDecoder);
                    Loggers.debug("Decoded value avro:" + _read.toString());


                    final Date now = new Date();
                    final Calendar _week = Calendar.getInstance();
                    _week.setTimeInMillis(now.getTime() + (7 * 24 * 60 * 60 * 1000));
                    final SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    _simpleDateFormat.format(_week.getTime());

                    final StringBuffer eventList = new StringBuffer("");

                    {//Foursquare

                        try {

                            _simpleDateFormat.format(_week.getTime());

                            final JSONObject jsonObject = Modules.getModules().getFoursquareFactory()
                                    .getInstance("https://api.foursquare.com/v2/venues/explore")
                                    .get("",
                                            new HashMap<String, String>() {
                                                {//Don't worry, this is a static initializer of this map :)
                                                    put("ll", "" + _read.getLatitude() + "," + _read.getLongitude());
                                                    put("radius", "" + 50000);//meters
                                                    put("intent", "browse");
                                                    put("section", "topPicks");
                                                    put("client_secret", "PODRX5YWBSLAKAYRQ5CLPEPS3WHCXWFIJ3LXF3AKH4U1BDNI");
                                                    put("client_id", "25JZAK3TQPLIPUUXPIJWXQ5NSKSPTP4SYZLUZSCTZF3UJ4YX");
                                                }
                                            }

                                    );

                            Loggers.debug("Foursquare Reply:" + jsonObject.toString());

                            final JSONArray referralArray = jsonObject.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items");

                            final Document eventTemplateDocument = HTMLDocParser.getDocument(RBGet.getGlobalConfigKey("PAGEFILES") + SUBSCRIBER_EMAIL_PLACE);
                            final String eventTemplate = HTMLDocParser.convertNodeToHtml(HTMLDocParser.$("content", eventTemplateDocument));

                            for (int i = 0; i < referralArray.length(); i++) {

                                final JSONObject referral = referralArray.getJSONObject(i);
                                referral.getJSONObject("venue").getJSONObject("location").getDouble("lng");
                                referral.getJSONObject("venue").getJSONObject("location").getDouble("lat");
                                final String eventName = referral.getJSONObject("venue").getString("name");
                                final String eventUrl = referral.getJSONObject("venue").optString("url");
                                final String eventVenue = referral.getJSONObject("venue").getJSONObject("location").getString("address");
                                Loggers.debug("Event name:" + eventName);
                                eventList.append(eventTemplate
                                        .replace("_name_link_", !(("" + eventUrl).isEmpty()) ? eventUrl : ("https://www.google.com/search?q=" + eventName.replaceAll(" ", "+").replaceAll("-", "+")))
                                        .replace("_place_link_", "https://maps.googleapis.com/maps/api/staticmap?sensor=false&size=600x600&markers=color:blue%7Clabel:S%7C" + _read.getLatitude().toString() + "," + _read.getLongitude().toString())
                                        .replace("_name_", eventName)
                                        .replace("_place_", eventVenue)
                                        .replace("_date_", eventName));
                            }
                        } catch (final Throwable t) {
                            Loggers.error("Error appending Foursquare data to Geohash Subscriber", t);
                        }
                    }


                    final String template = HTMLDocParser.getDocumentAsString(RBGet.getGlobalConfigKey("PAGEFILES") + EMAIL_FRAME);
                    final Document email = HTMLDocParser.getDocument(RBGet.getGlobalConfigKey("PAGEFILES") + SUBSCRIBER_EMAIL);
                    final String _content = HTMLDocParser.convertNodeToHtml(HTMLDocParser.$("content", email));
                    final Parameter _unsubscribeLink = new Parameter("http://www.ilikeplaces.com/unsubscribe/").append(Unsubscribe.TYPE, Unsubscribe.Type.GeohashSubscribe.name(), true)
                            .append(Unsubscribe.VALUE, rowKey);
                    final String finalEmail = template.replace("_FrameContent_", _content.replace(" ___||_", eventList.toString())
                            .replace("_unsubscribe_link_", _unsubscribeLink.get()));
                    Loggers.debug("Final email:" + finalEmail);
                    sendMailLocal.sendAsHTML(_read.getEmailId().toString(), "Thank God it's Friday!", finalEmail);
                }


            }


            _geohashSubscriberHBaseCrudService.scan(new GeohashSubscriber(), _scanner);
        }

        Loggers.debug("Completed scanner");

    }
}

