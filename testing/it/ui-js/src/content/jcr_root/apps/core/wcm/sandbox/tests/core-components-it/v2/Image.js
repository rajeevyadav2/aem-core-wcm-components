/*******************************************************************************
 * Copyright 2016 Adobe Systems Incorporated
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

/**
 * Tests for the core text component
 */
;(function (h, $) {

    // shortcut
    var c = window.CQ.CoreComponentsIT.commons;
    var imageV1 = window.CQ.CoreComponentsIT.v1.Image;

    /**
     * v2 specifics
     */
    var titleSelector = "span.cmp-image__title";
    var tcExecuteBeforeTest = imageV1.tcExecuteBeforeTest(c.rtImage_v2, "core/wcm/sandbox/tests/components/test-page-v2");
    var tcExecuteAfterTest = imageV1.tcExecuteAfterTest();

    /**
     * The main test suite for Image Component
     */
    new h.TestSuite('Core Components - Image v2', {path: '/apps/core/wcm/sandbox/tests/core-components-it/v2/Image.js',
        execBefore:c.tcExecuteBeforeTestSuite,
        execInNewWindow : false})

        .addTestCase(imageV1.tcAddImage(tcExecuteBeforeTest, tcExecuteAfterTest))
        .addTestCase(imageV1.tcAddAltText(tcExecuteBeforeTest, tcExecuteAfterTest))
        .addTestCase(imageV1.tcSetLink(tcExecuteBeforeTest, tcExecuteAfterTest))
        .addTestCase(imageV1.tcSetCaption(titleSelector, tcExecuteBeforeTest, tcExecuteAfterTest))
        .addTestCase(imageV1.tcSetCaptionAsPopup(tcExecuteBeforeTest, tcExecuteAfterTest))
        .addTestCase(imageV1.tcSetImageAsDecorative(tcExecuteBeforeTest, tcExecuteAfterTest))

    ;
}(hobs, jQuery));
